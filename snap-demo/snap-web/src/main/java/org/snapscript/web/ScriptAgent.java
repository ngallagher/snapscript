package org.snapscript.web;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URI;
import java.util.SortedSet;
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
import org.snapscript.web.ScriptProfiler.ProfileResult;

public class ScriptAgent {

   public final String SOURCE =
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

   private final Context context;
   private final ResourceCompiler compiler;
   private final ScriptProfiler profiler;
   private final int port;

   public ScriptAgent(URI rootURI, int port) {
      this.context = new ScriptAgentContext(rootURI);
      this.compiler = new ResourceCompiler(context);
      this.profiler = new ScriptProfiler();
      this.port = port;
   }
   
   public void run() throws Exception {
      try {
         System.err.println(SOURCE);
         long start = System.currentTimeMillis();
         PackageLinker linker = context.getLinker();
         Package library = linker.link("moduleForTheScriptAgent", SOURCE, "script");
         Module module = context.getBuilder().create("moduleForTheScriptAgent");
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
      TraceAnalyzer analyzer = context.getAnalyzer();
      analyzer.register(profiler);
      try {
         Socket socket = new Socket("localhost", port);
         ClientListener listener = new ClientListener(socket);
         listener.start();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private class ClientListener extends Thread {
      
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
                     SortedSet<ProfileResult> lines = profiler.lines();
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
            Executable executable = compiler.compile(filePath);
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

   public static void main(String[] list) throws Exception {
      new ScriptAgent(URI.create(list[0]), Integer.parseInt(list[1])).run();
   }
}
