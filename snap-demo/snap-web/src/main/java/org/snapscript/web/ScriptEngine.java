package org.snapscript.web;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.compile.FileContext;
import org.snapscript.compile.ResourceCompiler;
import org.snapscript.core.Context;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

/**
 * Create a pool of {@link ScriptAgent} processes that will stand idle waiting
 * for scripts to be dispatched for execution. This should speed up the time
 * it takes to run a script.
 */
public class ScriptEngine {

   private final Map<String, BlockingQueue<AgentConnection>> connections;
   private final AtomicReference<AgentConnection> current;
   private final ScriptEngineListener listener;
   private final AgentPoolLauncher launcher;
   private final AgentServer server;
   private final AtomicBoolean active;

   public ScriptEngine(ScriptEngineListener listener, int listenPort, int commandPort, int agentPool) throws Exception {
      this.connections = new ConcurrentHashMap<String, BlockingQueue<AgentConnection>>();
      this.current = new AtomicReference<AgentConnection>();
      this.launcher = new AgentPoolLauncher("http://localhost:"+listenPort+"/", commandPort, agentPool);
      this.server = new AgentServer(commandPort);
      this.active = new AtomicBoolean();
      this.listener = listener;
      
      // add a default one
      connections.put(System.getProperty("os.name"), new LinkedBlockingQueue<AgentConnection>());
   }
   
   public Set<String> getOperatingSystems() {
      Set<String> set = new HashSet<String>();
      set.addAll(connections.keySet());
      return set;
   }

   // launch a task and wait for it to finish
   public ScriptTask executeScript(ConsoleWriter output, ConsoleWriter info, File file, String os) {
      AgentListener listener = new AgentListener(output, info);

      try {
         killCurrentProcess(); // stop anything currently running
         return launchNewProcess(file, listener, os); // launch a new script
      }catch(Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private ScriptTask launchNewProcess(File file, AgentListener listener, String os) {
      try {
         AgentConnection conn = connections.get(os).poll(5, TimeUnit.SECONDS); // take a process from the pool
         
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
      private final File file;
      private final String path;
      
      public AgentConsoleReader(Socket socket, AgentListener listener, File file, String path) {
         this.done = new AtomicBoolean();
         this.listener = listener;
         this.socket = socket;
         this.file = file;
         this.path = path;
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
            parse();
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
      
      private void parse() {
         try {
            SyntaxCompiler compiler = new SyntaxCompiler();
            long start = System.nanoTime();
            SyntaxParser parser = compiler.compile();
            String source = load(file);
            SyntaxNode node = parser.parse(path, source, "script");
            node.getNodes();
            long finish = System.nanoTime();
            long duration = finish - start;
            long millis = TimeUnit.NANOSECONDS.toMillis(duration);
            listener.onUpdate("info", "Time taken to parse was " + millis + " ms, size was " + file.length());
         }catch(Exception e) {
            listener.onUpdate("info", ExceptionBuilder.build(e));
            throw new RuntimeException("Script does not compile", e);
         }
      }
      
      private void compile() {
         try {
            String name = file.getName();
            File root = file.getParentFile();
            Context context =new FileContext(root);
            ResourceCompiler compiler = new ResourceCompiler(context);
            long start = System.nanoTime();
            compiler.compile(name);
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
            String source = load(file);
            String syntax = SyntaxPrinter.print(parser, source, "script");
            listener.onUpdate("info", syntax);
         }catch(Exception e){
            //ignore for now
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
   }
   
   private class AgentConnection  {
      
      private final Socket socket;
      private final String os;
      
      public AgentConnection(Socket socket, String os) {
         this.socket = socket;
         this.os = os;
      }
      
      public synchronized String getOperatingSystem() {
         return os;
      }
      
      public synchronized ScriptTask execute(File script, AgentListener listener) {
         try {
            socket.setSoTimeout(10000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            File currentPath = new File(".");
            String relativePath = script.getCanonicalPath().substring(currentPath.getCanonicalPath().length());
            relativePath=relativePath.replace(File.separatorChar, '/');
            System.err.println("sending file '"+script.getCanonicalPath()+"' as '"+relativePath+"'");
            out.writeUTF("type=execute");
            out.writeUTF(relativePath.replace(File.separatorChar, '/'));
            AgentConsoleReader reader = new AgentConsoleReader(socket, listener, script, relativePath);
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
      
      private final String rootURI;
      private final int commandPort;
      private final int agentPool;
      
      public AgentPoolLauncher(String rootURI, int commandPort, int agentPool) {
         this.agentPool = agentPool;
         this.commandPort = commandPort;
         this.rootURI = rootURI;
      }
      
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
         Set<String> osSet = connections.keySet();
         
         try {
            List<AgentConnection> ready = new ArrayList<AgentConnection>();
            int require = agentPool;
            
            for(String os : osSet) {
               BlockingQueue<AgentConnection> connections = ScriptEngine.this.connections.get(os);
               
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
               int remaining = require - pool;
               
               for(int i = 0; i < remaining; i++) {
                  launch();
               }
               connections.addAll(ready);
            }
         }catch(Exception e){
            e.printStackTrace();
         }
      }
      
      public void launch() {
         try {
            BlockingQueue<AgentConnection> queue = connections.get(System.getProperty("os.name"));
            
            if(queue == null || queue.size() < agentPool) {
               String javaHome = System.getProperty("java.home");
               String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
               String classpath = System.getProperty("java.class.path");
               String className = ScriptAgent.class.getCanonicalName();
               ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className, String.valueOf(commandPort));
               builder.redirectErrorStream(true);
               builder.start();
            }
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   private class AgentServer implements Runnable {
      
      private final int commandPort;
      
      public AgentServer(int commandPort) {
         this.commandPort = commandPort;
      }
    
      public void run() {
         try {
            ServerSocket sock = new ServerSocket(commandPort);
            while(true) {
               Socket socket = sock.accept();
               DataInputStream in = new DataInputStream(socket.getInputStream());
               String os = in.readUTF();
               AgentConnection connection = new AgentConnection(socket, os);
               BlockingQueue<AgentConnection> queue = connections.get(os);
               
               // add to menu
               listener.onJoin(os);
               
               if(queue == null) {
                  queue = new LinkedBlockingQueue<AgentConnection>();
                  connections.put(os, queue);
               }
               queue.offer(connection);
               Thread.sleep(100);
            }
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
}
