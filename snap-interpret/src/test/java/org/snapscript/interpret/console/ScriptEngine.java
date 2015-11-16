package org.snapscript.interpret.console;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.snapscript.assemble.InstructionResolver;
import org.snapscript.assemble.ScriptCompiler;
import org.snapscript.assemble.ClassPathContext;
import org.snapscript.core.Context;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Model;
import org.snapscript.interpret.InterpretationResolver;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxParser;

/**
 * Create a pool of {@link ScriptAgent} processes that will stand idle waiting
 * for scripts to be dispatched for execution. This should speed up the time
 * it takes to run a script.
 */
public class ScriptEngine {

   public static final int PORT = 4456;
   public static final int POOL = 4;
  
   private final BlockingQueue<AgentConnection> connections;
   private final AtomicReference<AgentConnection> current;
   private final AgentPoolLauncher launcher;
   private final AgentServer server;
   private final AtomicBoolean active;
   private final JFrame frame;
   
   public ScriptEngine(JFrame frame) {
      this.connections = new LinkedBlockingQueue<AgentConnection>();
      this.current = new AtomicReference<AgentConnection>();
      this.launcher = new AgentPoolLauncher();
      this.server = new AgentServer();
      this.active = new AtomicBoolean();
      this.frame = frame;
   }

   // launch a task and wait for it to finish
   public ScriptTask executeScript(ConsoleWriter output, ConsoleWriter info, File file) {
      AgentListener listener = new AgentListener(output, info);

      try {
         killCurrentProcess(); // stop anything currently running
         return launchNewProcess(file, listener); // launch a new script
      }catch(Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private ScriptTask launchNewProcess(File file, AgentListener listener) {
      try {
         AgentConnection conn = connections.poll(5, TimeUnit.SECONDS); // take a process from the pool
         
         if(conn == null) {
            throw new IllegalStateException("Unable to execute " + file + " as agent pool is empty");
         }
         try {
            current.set(conn); // ensure we can stop the agent if needed
            return conn.execute(file, listener);
         }catch(Exception e) {
            e.printStackTrace();
         }
      }catch(Exception e){
         e.printStackTrace();
      }
      return null;
   }
   
   public void killCurrentProcess() {
      AgentConnection curr = current.get(); // stop any current process
      if(curr != null) {
         try {
            curr.stop();
            current.set(null);
         }catch(Exception e){
            e.printStackTrace();
         }
      }
   }
   
   public void start() {
      if(active.compareAndSet(false, true)) {
         new Thread(launcher, "AgentPoolLauncher").start();
         new Thread(server, "AgentServer").start();
      }
   }
   
   
   private class AgentListener {
      
      private final ConsoleWriter output;
      private final ConsoleWriter info;
      
      public AgentListener(ConsoleWriter output, ConsoleWriter info){
         this.output = output;
         this.info = info;
      }
      
      public void onUpdate(String type, String text){
         if(type.equals("info")) {
            info.log(text);
         } else if(type.equals("output")) {
            output.log(text);
         } else {
            System.err.println(text);
         }
      }
   }
   
   private class AgentConsoleReader implements ScriptTask, Runnable {
      
      private final AgentListener listener;
      private final AtomicBoolean done;
      private final Socket socket;
      private final String file;
      
      public AgentConsoleReader(Socket socket, AgentListener listener, String file) {
         this.done = new AtomicBoolean();
         this.listener = listener;
         this.socket = socket;
         this.file = file;
      }
      
      public void stop(){
         done.set(true);
         try {
            socket.getOutputStream().close();
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
      
      public void run() {
         try {
            compile();
            syntax();
            socket.setSoTimeout(0);
            InputStreamReader reader = new InputStreamReader(socket.getInputStream(),"UTF-8");
            BufferedReader buffer = new BufferedReader(reader);
            while(!done.get()) {
               String line = buffer.readLine();
               
               if(line != null) {
                  listener.onUpdate("output", line);
               } else {
                  break;
               }
            }
         }catch(Exception e){
            e.printStackTrace();
         }finally {
            try {
               socket.close();
            }catch(Exception ex){
               ex.printStackTrace();
            }
         }
      }
      
      private void compile() {
         try {
            InstructionResolver set = new InterpretationResolver();
            Model model = new EmptyModel();
            Context context =new ClassPathContext(set, model);
            ScriptCompiler compiler = new ScriptCompiler(context);
            long start = System.nanoTime();
            compiler.compile(file);
            long finish = System.nanoTime();
            long duration = finish - start;
            long millis = TimeUnit.NANOSECONDS.toMillis(duration);
            listener.onUpdate("info", "Time taken to compile was " + millis + " ms, size was " + file.length());
         }catch(Exception e) {
            listener.onUpdate("info", ExceptionBuilder.build(e));
            throw new RuntimeException("Script does not compile", e);
         }
      }
      
      private void syntax(){
         try {
            SyntaxCompiler analyzer = new SyntaxCompiler();
            SyntaxParser parser = analyzer.compile();
            String syntax = SyntaxPrinter.print(parser, file, "script");
            listener.onUpdate("info", syntax);
         }catch(Exception e){
            //ignore for now
         }
      }
   }
   
   private class AgentConnection  {
      
      private final Socket socket;
      
      public AgentConnection(Socket socket) {
         this.socket = socket;
      }
      
      public synchronized ScriptTask execute(File script, AgentListener listener) {
         try {
            socket.setSoTimeout(10000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String source = load(script);
            out.writeUTF("type=execute");
            out.writeUTF(script.getCanonicalPath());
            AgentConsoleReader reader = new AgentConsoleReader(socket, listener, source);
            Thread thread = new Thread(reader, "AgentConsoleReader");
            thread.start();
            return reader;
         }catch(Exception e) {
            e.printStackTrace();
            try {
               socket.close();
            }catch(Exception ex) {
               ex.printStackTrace();
            }
            throw new IllegalStateException("Could not execute script ["+script+"]", e);
         }
      }
      
      private synchronized String load(File source) throws Exception {
         FileInputStream in = new FileInputStream(source);
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
      
      public synchronized boolean ping() {
         try {
            socket.setSoTimeout(10000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF("type=ping");
            String response = in.readUTF();
            if(!response.equals("type=pong")) {
               socket.close(); // kills the agent;
               return false;
            }
            return true;
         }catch(Exception e) {
            e.printStackTrace();
            try {
               socket.close();
            }catch(Exception ex) {
               ex.printStackTrace();
            }
         }
         return false;
      }
      
      public synchronized void stop() {
         try {
            socket.getOutputStream().write(1);
            socket.getOutputStream().close();
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   private class AgentPoolLauncher implements Runnable {
      
      public void run() {
         while(true) {
            try {
               Thread.sleep(5000);
               ping();
            }catch(Exception e) {
               e.printStackTrace();
            }
         }
      }
      
      private void ping() {
         try {
            List<AgentConnection> ready = new ArrayList<AgentConnection>();
            int require = POOL;
            
            for(int i = 0; i < require; i++) {
               AgentConnection connection = connections.poll();
               
               if(connection == null) {
                  break;
               }
               if(connection.ping()) {
                  ready.add(connection);
               }
            }
            final int pool = ready.size();
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  String title = frame.getTitle();
                  Pattern pattern = Pattern.compile("(.*) agents=\\d+.*");
                  Matcher matcher = pattern.matcher(title);
                        
                  if(matcher.matches()) {
                     frame.setTitle(matcher.group(1) + " agents="+pool);
                  } else {
                     frame.setTitle(title + " agents="+pool);
                  }
               }
            });
            int remaining = require - pool;
            
            for(int i = 0; i < remaining; i++) {
               launch();
            }
            connections.addAll(ready);
         }catch(Exception e){
            e.printStackTrace();
         }
      }
      
      public void launch() {
         try {
            String javaHome = System.getProperty("java.home");
            String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
            String classpath = System.getProperty("java.class.path");
            String className = ScriptAgent.class.getCanonicalName();
            ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className, String.valueOf(PORT));
            builder.redirectErrorStream(true);
            builder.start();
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   private class AgentServer implements Runnable {
    
      public void run() {
         try {
            ServerSocket sock = new ServerSocket(PORT);
            while(true) {
               Socket socket = sock.accept();
               AgentConnection connection = new AgentConnection(socket);
               connections.offer(connection);
               Thread.sleep(100);
            }
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
}
