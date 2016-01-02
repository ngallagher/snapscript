package org.snapscript.ide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.snapscript.compile.Executable;
import org.snapscript.compile.ResourceCompiler;
import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Package;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TraceInterceptor;

public class ScriptAgent {

   public static final Map<String, String> CONTENT_TYPES;
   public static final File CLASSPATH_PATH;
   public static final File CLASSPATH_TEMP_PATH;
   public static final URI CLASSPATH_ROOT;
   public static final int CLASSPATH_PORT = 4457;
   public static final int COMMAND_PORT = 4456;
   public static final int AGENT_POOL = 4;
  
   static {
      try{
         CLASSPATH_PATH = new File("C:\\Work\\development\\github\\snapscript\\snap-demo");
         CLASSPATH_TEMP_PATH = new File("C:\\Work\\development\\github\\snapscript\\snap-demo\\snap-ide");
         CONTENT_TYPES = new ConcurrentHashMap<String, String>();
         CLASSPATH_ROOT = new URI("http://"+InetAddress.getLocalHost().getCanonicalHostName()+":"+CLASSPATH_PORT+"/");
         CONTENT_TYPES.put(".snap", "text/plain");
         CONTENT_TYPES.put(".html", "text/html");
         CONTENT_TYPES.put(".css", "text/css");
         CONTENT_TYPES.put(".json", "application/json");
         CONTENT_TYPES.put(".js", "application/javascript");
         CONTENT_TYPES.put(".png", "image/png");
         CONTENT_TYPES.put(".gif", "image/gif");
         CONTENT_TYPES.put(".jpg", "image/jpeg");
      }catch(Exception e){
         throw new InternalError("Invalid root");
      }
   }
   public static final Context CONTEXT = new ScriptAgentContext(CLASSPATH_ROOT);
   public static final ResourceCompiler COMPILER = new ResourceCompiler(CONTEXT);
   public static final Profiler INTERCEPTOR = new Profiler();
   public static final String SOURCE =
   "class InternalTypeForScriptAgent {\n"+
   "   static const ARR = [\"a\",\"b\",\"c\"];\n"+
   "   var x;\n"+
   "   new(index){\n"+
   "      this.x=ARR[index];\n"+
   "   }\n"+
   "   dump(){\n"+
   "      System.err.println(x);\n"+
   "   }\n"+
   "}\n"+
   "var privateVariableInScriptAgent = new InternalTypeForScriptAgent(1);\n"+
   "privateVariableInScriptAgent.dump();\n"+
   "System.err.println(privateVariableInScriptAgent.x);\n"+
   "System.err.println(InternalTypeForScriptAgent.ARR);";
   
   public static void main(String[] list) throws Exception {
      if(list.length > 0) {
         run(Integer.parseInt(list[0]));
      }else{
         run(COMMAND_PORT);
      }
   }
   
   public static void run(int serverPort) throws Exception {
      try {
         System.err.println(SOURCE);
         long start = System.currentTimeMillis();
         PackageLinker linker = CONTEXT.getLinker();
         Package library = linker.link("moduleForTheScriptAgent", SOURCE, "script");
         Module module = CONTEXT.getBuilder().create("moduleForTheScriptAgent");
         Scope scope = module.getScope();
         Statement script = library.compile(scope);
         long middle = System.currentTimeMillis();
         script.execute(scope);
         long finish = System.currentTimeMillis();
         System.err.println("Compile time="+(middle-start));
         System.err.println("Execute time="+(finish-middle));
      }catch(Exception e) {
         e.printStackTrace();
      }
      TraceAnalyzer analyzer = CONTEXT.getAnalyzer();
      analyzer.register(INTERCEPTOR);
      try {
         Socket socket = new Socket("localhost", serverPort);
         ClientListener listener = new ClientListener(socket);
         listener.start();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }
   
   private static class Profiler implements TraceInterceptor {
      
      private int[] counts;
      private long[] start;
      private long[] times;
      private int max;
      
      public Profiler() {
         this.start = new long[500];
         this.counts = new int[500];
         this.times = new long[500];
         this.max = 0;
      }

      public SortedSet<ProfileResult> lines() {
         SortedSet<ProfileResult> result=new TreeSet<ProfileResult>();
       
         for(int i = 0; i < max; i++){
            if(times[i] > 0) {
               result.add(new ProfileResult(times[i], i));
            }
         }
         return result;
      }
      
      @Override
      public void before(Scope scope, Object instruction, int line, int key) {
         // thread local required, also recursion counter
         if(times.length < line) {
            counts = copyOf(counts, line + 50);
            times = copyOf(times, line + 50);
            start = copyOf(start, line + 50);
         }
         int currentCount = counts[line]++;// we just entered an instruction
         
         if(currentCount == 0) {
            start[line] = System.nanoTime(); // first instruction to enter
         }
      }
      
      private int[] copyOf(int[] array, int newSize) {
         int[] copy = new int[newSize];
         System.arraycopy(array, 0, copy, 0, Math.min(newSize, array.length));
         return copy;
      }
      
      private long[] copyOf(long[] array, int newSize) {
         long[] copy = new long[newSize];
         System.arraycopy(array, 0, copy, 0, Math.min(newSize, array.length));
         return copy;
      }

      @Override
      public void after(Scope scope, Object instruction, int line, int key) {
         int currentCount = --counts[line]; // exit instruction
         
         if(currentCount == 0) {
            times[line] += (System.nanoTime() - start[line]);
            start[line] = 0L; // reset as we are now at zero
         }
         if(line > max) {
            max=line;
         }
      }

      
   }
   private static class ProfileResult implements Comparable<ProfileResult>{
      private final Long time;
      private final Integer line;
      
      private ProfileResult(Long time, Integer line) {
         this.time = time;
         this.line = line;
      }
      @Override
      public int compareTo(ProfileResult e) {
         int compare = e.time.compareTo(time);
         if(compare == 0) {
            return e.line.compareTo(line);
         }
         return compare;
      }
      public int getLine(){
         return line;
      }
      public long getTime(){
         return TimeUnit.NANOSECONDS.toMillis(time);
      }
   }
   private static class ClientListener extends Thread {
      
      private final Socket socket;
      
      public ClientListener(Socket socket) throws Exception {
         this.socket = socket;
      }
      
      public void run() {
         try {
            socket.setSoTimeout(0);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF(System.getProperty("os.name"));
            out.flush();
            while(true) {
               String request = in.readUTF();
               if(request.equals("type=execute")) {
                  String filePath = in.readUTF();
                  //String source = load(filePath);
                  try {
                     execute(filePath); // execute the script
                  } catch(Exception e) {
                     e.printStackTrace();
                  }finally {
                     System.err.flush(); // flush output to sockets
                     System.out.flush();
                     Thread.sleep(200);
                     // should really be a heat map for the editor
                     SortedSet<ProfileResult> lines = INTERCEPTOR.lines();
                     System.err.println();
                     for(ProfileResult entry : lines) {
                        int line = entry.getLine();
                        long time = entry.getTime();
                        System.err.println("Line " + line + " took " + time + " ms");
                     }
                     System.err.flush();
                     Thread.sleep(2000);
                     System.err.close();
                     System.out.close();
                     System.exit(0); // shutdown when finished
                  }
               }else if(!request.equals("type=ping")) {
                  socket.close(); // kills the agent;
               }
               out.writeUTF("type=pong");
            }
         }catch(Exception e) {
            e.printStackTrace();
         }finally{
            System.exit(0);
         }
      }

      private void execute(String filePath) {
         try {
            TerminateListener listener = new TerminateListener(socket);
            // redirect all output to the streams
            System.setOut(new PrintStream(socket.getOutputStream(), false, "UTF-8"));
            System.setErr(new PrintStream(socket.getOutputStream(), false, "UTF-8"));
            
            // start and listen for the socket close
            listener.start();
            long start = System.nanoTime();
            Executable executable = COMPILER.compile(filePath);
            long middle = System.nanoTime();
            executable.execute();
            long stop = System.nanoTime();
            System.out.flush();
            System.err.flush();
            System.err.println();
            System.err.println("Compile took "+
                  TimeUnit.NANOSECONDS.toMillis(middle-start) + 
                  " ms");
            System.err.println("Execute took "+
                  TimeUnit.NANOSECONDS.toMillis(stop-middle) +
                  " ms");
            
         } catch (Exception e) {
            System.err.println(ExceptionBuilder.build(e));
         }
      }
   }
   
   private static class TerminateListener extends Thread {
      
      private final Socket socket;
      
      public TerminateListener(Socket socket){
         this.socket = socket;
      }
      
      public void run() {
         try {
            socket.setSoTimeout(0); // wait forever
            socket.getInputStream().read();
         }catch(Exception e){
            e.printStackTrace();
         }finally {
            System.exit(0);
         }
      }
   }
   
   private static class ExceptionBuilder {

      public static String build(Exception cause) {
         StringWriter w = new StringWriter();
         PrintWriter p = new PrintWriter(w);
         cause.printStackTrace(p);
         p.flush();
         p.close();
         return w.toString();
         
      }
   }

}
