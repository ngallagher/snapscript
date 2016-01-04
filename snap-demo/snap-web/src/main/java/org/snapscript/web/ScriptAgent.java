package org.snapscript.web;

import java.io.PrintStream;
import java.net.Socket;
import java.net.URI;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
import org.snapscript.web.message.Message;
import org.snapscript.web.message.MessageListener;
import org.snapscript.web.message.MessageOutputStream;
import org.snapscript.web.message.MessagePublisher;
import org.snapscript.web.message.MessageReceiver;
import org.snapscript.web.message.MessageType;

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
   private final String process;
   private final int port;

   public ScriptAgent(URI rootURI, String process, int port) {
      this.context = new ScriptAgentContext(rootURI);
      this.compiler = new ResourceCompiler(context);
      this.profiler = new ScriptProfiler();
      this.process = process;
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
         script.execute(scope);  // warm up the agent for quicker execution
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
         ClientListener listener = new ClientListener(socket, process);
         listener.start();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private class ClientListener extends Thread implements MessageListener {
      
      private final AtomicReference<String> reference;
      private final MessagePublisher publisher;
      private final MessageReceiver receiver;
      private final Socket socket;
      
      public ClientListener(Socket socket, String process) throws Exception {
         this.reference = new AtomicReference<String>(process);
         this.publisher = new MessagePublisher(reference, socket.getOutputStream());
         this.receiver = new MessageReceiver(this, socket);
         this.socket = socket;
      }
      
      public void run() {
         try {
            socket.setSoTimeout(0);
            publisher.publish(MessageType.REGISTER, System.getProperty("os.name"));
            receiver.start();
         }catch(Exception e) {
            e.printStackTrace();
         }finally{
            System.exit(0);
         }
      }

      @Override
      public void onMessage(Message message) {
         MessageType type = message.getType();
         
         if(type == MessageType.PING) {
            onPing(message);
         } else if(type == MessageType.SCRIPT) {
            onScript(message);
         } else if(type == MessageType.PROCESS_ID) {
            onProcessId(message);
         } else {
            onExit(message);
         }
      }
      
      private void onExit(Message message) {
         try {
            socket.close(); // kills the agent
         } catch(Exception e) {
            e.printStackTrace();
         }
      }
      
      private void onProcessId(Message message) {
         try {
            String processId = message.getData("UTF-8");
            reference.set(processId);
         } catch(Exception e){
            e.printStackTrace();
         }
      }
      
      private void onPing(Message message) {
         try {
            publisher.publish(MessageType.PONG, new byte[]{});
         } catch(Exception e){
            e.printStackTrace();
         }
      }
      
      private void onScript(Message message) {
         String filePath = message.getData("UTF-8");
         ExecuteTask task = new ExecuteTask(publisher, filePath);
         task.start();

      }

      @Override
      public void onError(Exception cause) {
         cause.printStackTrace(System.err);
      }

      @Override
      public void onClose() {
         System.exit(0);
      }
   }
   
   private class ExecuteTask extends Thread {
      
      private final MessagePublisher publisher;
      private final String filePath;
      
      public ExecuteTask(MessagePublisher publisher, String filePath) {
         this.publisher = publisher;
         this.filePath = filePath;
      }
      
      @Override
      public void run() {
         try {
            execute(); // execute the script
         } catch(Exception e) {
            e.printStackTrace();
         }finally {
            try {
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
            } catch(Exception e) {
               e.printStackTrace();
            } finally {
               System.exit(0); // shutdown when finished  
            }
         }
      }

      private void execute() {
         try {
            MessageOutputStream error = new MessageOutputStream(MessageType.PRINT_ERROR, publisher);
            MessageOutputStream output = new MessageOutputStream(MessageType.PRINT_OUTPUT, publisher);
            
            // redirect all output to the streams
            System.setOut(new PrintStream(output, false, "UTF-8"));
            System.setErr(new PrintStream(error, false, "UTF-8"));
            
            // start and listen for the socket close
            long start = System.nanoTime();
            Executable executable = compiler.compile(filePath);
            long middle = System.nanoTime();
            executable.execute();
            long stop = System.nanoTime();
            System.out.flush();
            System.err.flush();
            System.err.println();
            
            publisher.publish(MessageType.COMPILE_TIME, TimeUnit.NANOSECONDS.toMillis(middle-start));
            publisher.publish(MessageType.EXECUTE_TIME, TimeUnit.NANOSECONDS.toMillis(stop-middle));
         } catch (Exception e) {
            System.err.println(ExceptionBuilder.build(e));
         }
      }
   }

   public static void main(String[] list) throws Exception {
      new ScriptAgent(URI.create(list[0]), list[1], Integer.parseInt(list[2])).run();
   }
}
