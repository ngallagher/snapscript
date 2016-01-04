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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.compile.FileContext;
import org.snapscript.compile.ResourceCompiler;
import org.snapscript.core.Context;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;
import org.snapscript.web.message.Message;
import org.snapscript.web.message.MessageListener;
import org.snapscript.web.message.MessagePublisher;
import org.snapscript.web.message.MessageReceiver;
import org.snapscript.web.message.MessageType;

/**
 * Create a pool of {@link ScriptAgent} processes that will stand idle waiting
 * for scripts to be dispatched for execution. This should speed up the time
 * it takes to run a script.
 */
public class ScriptEngine {

   private final Map<String, BlockingQueue<AgentConnection>> connections;
   private final AtomicReference<AgentConnection> current;
   private final MessageListener listener;
   private final AgentPoolLauncher launcher;
   private final AgentServer server;
   private final AtomicBoolean active;

   public ScriptEngine(MessageListener listener, int listenPort, int commandPort, int agentPool) throws Exception {
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
   public ScriptTask executeScript(File file, String processId, String os) {
      try {
         killCurrentProcess(); // stop anything currently running
         return launchNewProcess(file, processId, os); // launch a new script
      }catch(Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private ScriptTask launchNewProcess(File file, String processId, String os) {
      try {
         AgentConnection conn = connections.get(os).poll(5, TimeUnit.SECONDS); // take a process from the pool
         
         if(conn == null) {
            throw new IllegalStateException("Unable to execute " + file + " as agent pool is empty");
         }
         try {
            current.set(conn); // ensure we can stop the agent if needed
            return conn.execute(file, processId);
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
   
   private class AgentConsoleReader implements ScriptTask, Runnable {
      
      private final AtomicBoolean done;
      private final String processId;
      private final Socket socket;
      private final File file;
      private final String path;
      
      public AgentConsoleReader(Socket socket, String processId, File file, String path) {
         this.done = new AtomicBoolean();
         this.processId = processId;
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
                  System.out.println(processId + ": " +line);
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
            System.out.println(processId + ": Time taken to parse was " + millis + " ms, size was " + file.length());
         }catch(Exception e) {
            System.out.println(processId + ": " + ExceptionBuilder.build(e));
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
            System.out.println(processId + ": Time taken to compile was " + millis + " ms, size was " + file.length());
         }catch(Exception e) {
            System.out.println(processId + ": " + ExceptionBuilder.build(e));
            throw new RuntimeException("Script does not compile", e);
         }
      }
      
      private void syntax(){
         try {
            SyntaxCompiler analyzer = new SyntaxCompiler();
            SyntaxParser parser = analyzer.compile();
            String source = load(file);
            String syntax = SyntaxPrinter.print(parser, source, "script");
            System.out.println(processId + ": " + syntax);
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
   
   private class AgentConnection implements MessageListener {
      
      private final MessageReceiver receiver;
      private final MessagePublisher publisher;
      private final Socket socket;
      
      public AgentConnection(MessagePublisher publisher, Socket socket) {
         this.receiver = new MessageReceiver(this, socket);
         this.publisher = publisher;
         this.socket = socket;
      }
      
      public synchronized ScriptTask execute(File script, String processId) {
         try {
            socket.setSoTimeout(10000);
            File currentPath = new File(".");
            String relativePath = script.getCanonicalPath().substring(currentPath.getCanonicalPath().length());
            relativePath=relativePath.replace(File.separatorChar, '/');
            System.err.println("sending file '"+script.getCanonicalPath()+"' as '"+relativePath+"'");
            publisher.publish(MessageType.PROCESS_ID, processId); // register a process id
            publisher.publish(MessageType.SCRIPT, relativePath.replace(File.separatorChar, '/')); // send the script
            AgentConsoleReader reader = new AgentConsoleReader(socket, processId, script, relativePath);
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
      
      public synchronized void start() {
         receiver.start();
      }
      
      public synchronized void stop() {
         try {
            socket.getOutputStream().write(1);
            socket.getOutputStream().close();
         }catch(Exception e) {
            e.printStackTrace();
         }
      }

      @Override
      public void onMessage(Message message) {
         MessageType type = message.getType();
         
         if(type == MessageType.REGISTER) {
            onJoin(message);
         }
         listener.onMessage(message);
      }
      
      private void onJoin(Message message) {
         try {
            String os = message.getData("UTF-8");
            BlockingQueue<AgentConnection> queue = connections.get(os);
            
            if(queue == null) {
               queue = new LinkedBlockingQueue<AgentConnection>();
               connections.put(os, queue);
            }
            queue.offer(this);
            Thread.sleep(100);
         } catch(Exception e){
            e.printStackTrace();
         }
      }

      @Override
      public void onError(Exception cause) {
         cause.printStackTrace(System.err);
         stop();
      }

      @Override
      public void onClose() {
         stop();
      }
   }
   
   private class AgentPoolLauncher implements Runnable {
      
      private final AtomicLong counter;
      private final String rootURI;
      private final int commandPort;
      private final int agentPool;
      
      public AgentPoolLauncher(String rootURI, int commandPort, int agentPool) {
         this.counter = new AtomicLong();
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
               long agentId = counter.getAndIncrement();
               ProcessBuilder builder = new ProcessBuilder(javaBin, 
                     "-cp", 
                     classpath, 
                     className, 
                     rootURI,
                     String.valueOf(agentId),
                     String.valueOf(commandPort));
               builder.redirectErrorStream(true);
               builder.start();
            }
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   private class AgentServer implements Runnable {
      
      private final AtomicReference<String> reference;
      private final int commandPort;
      
      public AgentServer(int commandPort) {
         this.reference = new AtomicReference<String>("server");
         this.commandPort = commandPort;
      }
    
      public void run() {
         try {
            ServerSocket sock = new ServerSocket(commandPort);
            while(true) {
               Socket socket = sock.accept();
               MessagePublisher publisher = new MessagePublisher(reference, socket.getOutputStream());
               AgentConnection connection = new AgentConnection(publisher, socket);
               
               connection.start(); // start receiving messages
            }
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
}
