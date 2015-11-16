package org.snapscript.interpret.console;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.snapscript.compile.ClassPathContext;
import org.snapscript.compile.StringCompiler;
import org.snapscript.core.Context;
import org.snapscript.core.Executable;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;

public class ScriptProcess {

   private static final Map<String, Object> MAP = new HashMap<String, Object>();
   private static final Model MODEL = new MapModel(MAP);
   private static final Context CONTEXT = new ClassPathContext(MODEL);
   private static final StringCompiler COMPILER = new StringCompiler(CONTEXT);
   
   public static void main(String[] list) throws Exception {
      run(list[0]);
   }
   
   public static void run(String file) throws Exception {
      try {
         TerminateListener listener = new TerminateListener();
         listener.start();
         System.err.println("port="+listener.getPort()); // tell the launcher your port
      } catch (Exception e) {
         StringWriter w = new StringWriter();
         PrintWriter p = new PrintWriter(w);
         e.printStackTrace(p);
         p.flush();
         System.err.println(w.toString());
      }
      try {
         String source = load(file);
         Executable executable = COMPILER.compile(source);
         executable.execute();
      } catch (Exception e) {
         StringWriter w = new StringWriter();
         PrintWriter p = new PrintWriter(w);
         e.printStackTrace(p);
         p.flush();
         System.err.println(w.toString());
      }finally {
         System.exit(0);
      }
   }

   private static String load(String source) throws Exception {
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

   
   private static class TerminateListener extends Thread {
      
      private final ServerSocket socket;
      
      public TerminateListener() throws Exception {
         this.socket = new ServerSocket(0);
      }
      
      public int getPort() {
         return socket.getLocalPort();
      }
      
      public void run() {
         try {
            Socket sock = socket.accept();
            InputStream stream = sock.getInputStream();
            stream.read();
         }catch(Exception e) {
            e.printStackTrace();
         }finally{
            System.exit(0);
         }
      }
   }
   
}
