package org.snapscript.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.snapscript.web.message.MessageClient;
import org.snapscript.web.message.MessageListener;
import org.snapscript.web.message.MessageType;

/**
 * Create a pool of {@link WebScriptAgent} processes that will stand idle waiting
 * for scripts to be dispatched for execution. This should speed up the time
 * it takes to run a script.
 */
public class WebScriptEngine {
   
   private static final String RESOURCE_URL = "http://localhost:%s/resource";

   private final Map<String, BlockingQueue<AgentConnection>> connections;
   private final Map<String, AgentConnection> running;
   private final AtomicReference<MessageListener> listener;
   private final AgentPoolLauncher launcher;
   private final AgentServer server;
   private final AtomicBoolean active;

   public WebScriptEngine(int listenPort, int commandPort, int agentPool) throws Exception {
      this.connections = new ConcurrentHashMap<String, BlockingQueue<AgentConnection>>();
      this.running = new ConcurrentHashMap<String, AgentConnection>();
      this.listener = new AtomicReference<MessageListener>();
      this.launcher = new AgentPoolLauncher(String.format(RESOURCE_URL, listenPort), commandPort, agentPool);
      this.server = new AgentServer(commandPort);
      this.active = new AtomicBoolean();
      
      // add a default one
      connections.put(System.getProperty("os.name"), new LinkedBlockingQueue<AgentConnection>());
   }
   
   public void register(MessageListener listener){
      this.listener.set(listener);
   }
   
   public Set<String> getOperatingSystems() {
      Set<String> set = new HashSet<String>();
      set.addAll(connections.keySet());
      return set;
   }

   // launch a task and wait for it to finish
   public ScriptTask executeScript(File file, String project, String path, String processId, String os) {
      try {
         killCurrentProcess(); // stop anything currently running
         return launchNewProcess(file, project, path, processId, os); // launch a new script
      }catch(Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private ScriptTask launchNewProcess(File file, String project, String path, String processId, String os) {
      try {
         AgentConnection conn = connections.get(os).poll(5, TimeUnit.SECONDS); // take a process from the pool
         
         if(conn == null) {
            throw new IllegalStateException("Unable to execute " + file + " as agent pool is empty");
         }
         try {
            running.put(processId, conn); // ensure we can stop the agent if needed
            return conn.execute(file, project, path, processId);
         }catch(Exception e) {
            e.printStackTrace();
         }
      }catch(Exception e){
         e.printStackTrace();
      }
      return null;
   }
   
   public void killCurrentProcess() {
      Set<String> processIds = running.keySet();
      for(String processId : processIds) {
         AgentConnection next = running.remove(processId); // stop any current process
         if(next != null) {
            try {
               next.stop();
            }catch(Exception e){
               e.printStackTrace();
            }
         }
      }
   }
   
   public void start() {
      if(active.compareAndSet(false, true)) {
         new Thread(launcher, "AgentPoolLauncher").start();
         new Thread(server, "AgentServer").start();
      }
   }
   
   private class AgentTask implements ScriptTask  {
      
      private final MessageClient client;
      private final AtomicBoolean done;
      private final String processId;
      private final File file;
      private final String path;
      
      public AgentTask(MessageClient client, String processId, File file, String path) {
         this.done = new AtomicBoolean();
         this.processId = processId;
         this.client = client;
         this.file = file;
         this.path = path;
      }
      
      public void stop(){
         done.set(true);
         try {
            client.close();
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
      
      public void start() {
         try {
            parse();
            compile();
            syntax();
            client.setTimeout(0);
         }catch(Exception e){
            e.printStackTrace();
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
      
      private final MessageClient client;
      
      public AgentConnection(MessageClient client) {
         this.client = client;
      }
      
      public synchronized ScriptTask execute(File script, String project, String path, String processId) {
         try {
            client.setTimeout(10000);
            System.err.println("sending file '"+script.getCanonicalPath()+"' as '"+path+"'");
            client.getPublisher().publish(MessageType.PROCESS_ID, processId); // register a process id
            client.getPublisher().publish(MessageType.PROJECT_NAME, project); // send the script
            client.getPublisher().publish(MessageType.SCRIPT, path); // send the script
            AgentTask task = new AgentTask(client, processId, script, path);
            task.start();
            return task;
         }catch(Exception e) {
            e.printStackTrace();
            try {
               client.close();
            }catch(Exception ex) {
               ex.printStackTrace();
            }
            throw new IllegalStateException("Could not execute script ["+script+"]", e);
         }
      }
      
      public synchronized boolean ping() {
         try {
            client.setTimeout(10000);
            client.getPublisher().publish(MessageType.PING, new byte[]{},0,0);
            return true;
         }catch(Exception e) {
            System.err.println("PING FAILURE ["+e.getMessage()+"]");
            try {
               client.close();
            }catch(Exception ex) {
               ex.printStackTrace();
            }
         }
         return false;
      }
      
      public synchronized void start() {
         client.start();
      }
      
      public synchronized void stop() {
         try {
            client.close();
         }catch(Exception e) {
            e.printStackTrace();
         }
      }

      @Override
      public void onMessage(Message message) {
         MessageType type = message.getType();
         String processId = message.getProcessId();
         
         System.err.println(type + " [" + processId + "]");
         
         if(type == MessageType.REGISTER) {
            onJoin(message);
         }
         if(listener.get()!=null){
            listener.get().onMessage(message);
         }
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
               BlockingQueue<AgentConnection> connections = WebScriptEngine.this.connections.get(os);
               
               while(!connections.isEmpty()) {
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
            ready.clear();
            Set<String> processIds = new HashSet<String>(running.keySet());
            for(String processId : processIds) {
               AgentConnection next = running.get(processId); // stop any current process
               
               if(next.ping()) {
                  ready.remove(processId);
               }
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
               String className = WebScriptAgent.class.getCanonicalName();
               long agentId = counter.getAndIncrement();
               ProcessBuilder builder = new ProcessBuilder(javaBin, 
                     "-cp", 
                     classpath, 
                     className, 
                     rootURI,
                     "agent-" + agentId,
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
      
      private final int commandPort;
      
      public AgentServer(int commandPort) {
         this.commandPort = commandPort;
      }
    
      public void run() {
         try {
            ServerSocket sock = new ServerSocket(commandPort);
            while(true) {
               Socket socket = sock.accept();
               MessageClient client = new MessageClient("server", socket);
               AgentConnection connection = new AgentConnection(client);
               
               client.register(connection);
               connection.start(); // start receiving messages
            }
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
}
