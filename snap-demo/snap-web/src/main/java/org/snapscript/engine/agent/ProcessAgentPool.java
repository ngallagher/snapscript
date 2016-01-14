package org.snapscript.engine.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.engine.event.ExitEvent;
import org.snapscript.engine.event.PongEvent;
import org.snapscript.engine.event.ProcessEventAdapter;
import org.snapscript.engine.event.ProcessEventChannel;
import org.snapscript.engine.event.ProcessEventListener;
import org.snapscript.engine.event.RegisterEvent;
import org.snapscript.engine.event.SyntaxErrorEvent;
import org.snapscript.engine.event.WriteErrorEvent;
import org.snapscript.engine.event.WriteOutputEvent;
import org.snapscript.engine.event.socket.SocketEventServer;

public class ProcessAgentPool {

   private final Cache<String, BlockingQueue<ProcessAgentConnection>> connections;
   private final Cache<String, ProcessEventListener> listeners;
   private final BlockingQueue<ProcessAgentConnection> running;
   private final ProcessEventInterceptor interceptor;
   private final ProcessAgentLauncher launcher;
   private final ProcessAgentPinger pinger;
   private final SocketEventServer server;
   private final int capacity;
   
   public ProcessAgentPool(String root, int port, int capacity) throws IOException {
      this(root, port, capacity, 5000);
   }
   
   public ProcessAgentPool(String root, int port, int capacity, long frequency) throws IOException {
      this.connections = new LeastRecentlyUsedCache<String, BlockingQueue<ProcessAgentConnection>>();
      this.listeners = new LeastRecentlyUsedCache<String, ProcessEventListener>();
      this.running = new LinkedBlockingQueue<ProcessAgentConnection>();
      this.interceptor = new ProcessEventInterceptor(listeners);
      this.launcher = new ProcessAgentLauncher(root, port);
      this.pinger = new ProcessAgentPinger(frequency);
      this.server = new SocketEventServer(interceptor, port);
      this.capacity = capacity;
   }
   
   public ProcessAgentConnection acquire(ProcessEventListener listener, String system) {
      try {
         BlockingQueue<ProcessAgentConnection> pool = connections.fetch(system);
         
         if(pool == null) {
            throw new IllegalArgumentException("No pool of type '" + system + "'");
         }
         ProcessAgentConnection connection = pool.poll(5, TimeUnit.SECONDS); // take a process from the pool
         
         if(connection == null) {
            throw new IllegalStateException("No agent of type " + system + " as pool is empty");
         }
         String name = connection.toString();
         
         listeners.cache(name, listener);
         running.offer(connection);
         return connection;
      }catch(Exception e){
         e.printStackTrace();
      }
      return null;
   }
   
   public void start() {
      try {
         server.start();
         pinger.start();
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   private class ProcessEventInterceptor extends ProcessEventAdapter {
      
      private final Cache<String, ProcessEventListener> listeners;
      
      public ProcessEventInterceptor(Cache<String, ProcessEventListener> listeners) {
         this.listeners = listeners;
      }
      
      @Override
      public void onRegister(ProcessEventChannel channel, RegisterEvent event) throws Exception {
         String process = event.getProcess();
         String system = event.getSystem();
         ProcessAgentConnection connection = new ProcessAgentConnection(channel, process);
         BlockingQueue<ProcessAgentConnection> pool = connections.fetch(system);
         
         if(pool == null) {
            pool = new LinkedBlockingQueue<ProcessAgentConnection>();
            connections.cache(system, pool);
         }
         pool.offer(connection);
      }
      
      @Override
      public void onExit(ProcessEventChannel channel, ExitEvent event) throws Exception {
         String process = event.getProcess();
         ProcessEventListener listener = listeners.fetch(process);
         
         if(listener != null) {
            try {
               listener.onExit(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.take(process);
               channel.close();
            }
         }
      }
      
      @Override
      public void onWriteError(ProcessEventChannel channel, WriteErrorEvent event) throws Exception {
         String process = event.getProcess();
         ProcessEventListener listener = listeners.fetch(process);
         
         if(listener != null) {
            try {
               listener.onWriteError(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.take(process);
               channel.close();
            }
         }
      }
      
      @Override
      public void onWriteOutput(ProcessEventChannel channel, WriteOutputEvent event) throws Exception {
         String process = event.getProcess();
         ProcessEventListener listener = listeners.fetch(process);
         
         if(listener != null) {
            try {
               listener.onWriteOutput(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.take(process);
               channel.close();
            }
         }
      }
      
      @Override
      public void onSyntaxError(ProcessEventChannel channel, SyntaxErrorEvent event) throws Exception {
         String process = event.getProcess();
         ProcessEventListener listener = listeners.fetch(process);
         
         if(listener != null) {
            try {
               listener.onSyntaxError(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.take(process);
               channel.close();
            }
         }
      }
      
      @Override
      public void onPong(ProcessEventChannel channel, PongEvent event) throws Exception {
         String process = event.getProcess();
         ProcessEventListener listener = listeners.fetch(process);
         
         if(listener != null) {
            try {
               listener.onPong(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.take(process);
               channel.close();
            }
         }
      }
   }
   
   private class ProcessAgentPinger extends Thread {
   
      private final long frequency;
      
      public ProcessAgentPinger(long frequency) {
         this.frequency = frequency;
      }
      
      @Override
      public void run() {
         while(true) {
            try {
               String host = System.getProperty("os.name");
               BlockingQueue<ProcessAgentConnection> pool = connections.fetch(host);
               
               if(pool == null) {
                  pool = new LinkedBlockingQueue<ProcessAgentConnection>();
                  connections.cache(host, pool);
               }
               Thread.sleep(frequency);
               ping();
            }catch(Exception e) {
               e.printStackTrace();
            }
         }
      }
      
      private void ping() {
         Set<String> systems = connections.keySet();
         
         try {
            List<ProcessAgentConnection> active = new ArrayList<ProcessAgentConnection>();
            int require = capacity;
            
            for(String system : systems) {
               BlockingQueue<ProcessAgentConnection> available = connections.fetch(system);
               
               while(!connections.isEmpty()) {
                  ProcessAgentConnection connection = available.poll();
                  
                  if(connection == null) {
                     break;
                  }
                  if(connection.ping()) {
                     active.add(connection);
                  }
               }
               int pool = active.size();
               int remaining = require - pool;
               
               for(int i = 0; i < remaining; i++) {
                  launcher.launch();
               }
               available.addAll(active);
            }
            active.clear();
            
            while(!connections.isEmpty()) {
               ProcessAgentConnection connection = running.poll();
               
               if(connection == null) {
                  break;
               }
               if(connection.ping()) {
                  active.add(connection);
               }
            }
            running.addAll(active);
         }catch(Exception e){
            e.printStackTrace();
         }
      }
   }
}