package org.snapscript.interpret.console;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.snapscript.compile.ClassPathContext;
import org.snapscript.compile.StringCompiler;
import org.snapscript.core.Context;
import org.snapscript.core.Executable;
import org.snapscript.core.Package;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;

public class ScriptAgent {

   private static final Map<String, Object> MAP = new HashMap<String, Object>();
   private static final Model MODEL = new MapModel(MAP);
   private static final Context CONTEXT = new ClassPathContext(MODEL);
   private static final StringCompiler COMPILER = new StringCompiler(CONTEXT);
   private static final String SOURCE =
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
         run(ScriptEngine.PORT);
      }
   }
   
   public static void run(int serverPort) throws Exception {
      try {
         PackageLinker linker = CONTEXT.getLinker();
         Package library = linker.link("moduleForTheScriptAgent", SOURCE);
         Module module = CONTEXT.getBuilder().create("moduleForTheScriptAgent");
         Scope scope = module.getScope();
         
         library.include(scope);
      }catch(Exception e) {
         e.printStackTrace();
      }
      try {
         Socket socket = new Socket("localhost", serverPort);
         ClientListener listener = new ClientListener(socket);
         listener.start();
      } catch (Exception e) {
         e.printStackTrace();
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
            while(true) {
               String request = in.readUTF();
               if(request.equals("type=execute")) {
                  String filePath = in.readUTF();
                  String source = load(filePath);
                  try {
                     execute(source); // execute the script
                  } catch(Exception e) {
                     e.printStackTrace();
                  }finally {
                     System.err.flush(); // flush output to sockets
                     System.out.flush();
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

      private void execute(String script) {
         try {
            TerminateListener listener = new TerminateListener(socket);
            // redirect all output to the streams
            System.setOut(new PrintStream(socket.getOutputStream(), true, "UTF-8"));
            System.setErr(new PrintStream(socket.getOutputStream(), true, "UTF-8"));
            
            // start and listen for the socket close
            listener.start();
            Executable executable = COMPILER.compile(script);
            executable.execute();
         } catch (Exception e) {
            System.err.println(ExceptionBuilder.build(e));
         }
      }
      
      private String load(String source) throws Exception {
         File file = new File(source);
         FileInputStream in = new FileInputStream(file);
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         try {
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = in.read(buffer)) != -1) {
               out.write(buffer, 0, count);
            }
         } finally {
            in.close();
         }
         return out.toString();
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
  
}
