package org.snapscript.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.agent.event.BeginEvent;
import org.snapscript.agent.event.ExitEvent;
import org.snapscript.agent.event.PongEvent;
import org.snapscript.agent.event.ProcessEventAdapter;
import org.snapscript.agent.event.ProcessEventChannel;
import org.snapscript.agent.event.ProcessEventListener;
import org.snapscript.agent.event.ProfileEvent;
import org.snapscript.agent.event.RegisterEvent;
import org.snapscript.agent.event.ScopeEvent;
import org.snapscript.agent.event.SyntaxErrorEvent;
import org.snapscript.agent.event.WriteErrorEvent;
import org.snapscript.agent.event.WriteOutputEvent;
import org.snapscript.agent.event.socket.SocketEventServer;
import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;

public class ProcessAgentPool {

   private final Cache<String, BlockingQueue<ProcessAgentConnection>> connections;
   private final BlockingQueue<ProcessAgentConnection> running;
   private final Set<ProcessEventListener> listeners;
   private final ProcessAgentConfiguration configuration;
   private final ProcessEventInterceptor interceptor;
   private final ProcessAgentLauncher launcher;
   private final ProcessAgentPinger pinger;
   private final SocketEventServer server;
   private final int capacity;
   
   public ProcessAgentPool(ProcessAgentConfiguration configuration, int port, int capacity) throws IOException {
      this(configuration, port, capacity, 2000);
   }
   
   public ProcessAgentPool(ProcessAgentConfiguration configuration, int port, int capacity, long frequency) throws IOException {
      this.connections = new LeastRecentlyUsedCache<String, BlockingQueue<ProcessAgentConnection>>();
      this.listeners = new CopyOnWriteArraySet<ProcessEventListener>();
      this.running = new LinkedBlockingQueue<ProcessAgentConnection>();
      this.interceptor = new ProcessEventInterceptor(listeners);
      this.server = new SocketEventServer(interceptor, port);
      this.launcher = new ProcessAgentLauncher(server);
      this.pinger = new ProcessAgentPinger(frequency);
      this.configuration = configuration;
      this.capacity = capacity;
   }
   
   public ProcessAgentConnection acquire(String system) {
      try {
         BlockingQueue<ProcessAgentConnection> pool = connections.fetch(system);
         
         if(pool == null) {
            throw new IllegalArgumentException("No pool of type '" + system + "'");
         }
         ProcessAgentConnection connection = pool.poll(5, TimeUnit.SECONDS); // take a process from the pool
         
         if(connection == null) {
            throw new IllegalStateException("No agent of type " + system + " as pool is empty");
         }
         running.offer(connection);
         return connection;
      }catch(Exception e){
         e.printStackTrace();
      }
      return null;
   }
   
   public void register(ProcessEventListener listener) {
      try {
         listeners.add(listener);
      }catch(Exception e){
         e.printStackTrace();
      }
   }
   
   public void remove(ProcessEventListener listener) {
      try {
         listeners.remove(listener);
      }catch(Exception e){
         e.printStackTrace();
      }
   }
   
   public void start(String address) { // http://host:port/project
      try {
         server.start();
         pinger.start(address);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void launch() { // launch a new process!!
      try {
         pinger.launch();
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   private class ProcessEventInterceptor extends ProcessEventAdapter {
      
      private final Set<ProcessEventListener> listeners;
      
      public ProcessEventInterceptor(Set<ProcessEventListener> listeners) {
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
         for(ProcessEventListener listener : listeners) {
            try {
               listener.onExit(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.remove(listener);
            }
         }
      }
      
      @Override
      public void onWriteError(ProcessEventChannel channel, WriteErrorEvent event) throws Exception {
         for(ProcessEventListener listener : listeners) {
            try {
               listener.onWriteError(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.remove(listener);
            }
         }
      }
      
      @Override
      public void onWriteOutput(ProcessEventChannel channel, WriteOutputEvent event) throws Exception {
         for(ProcessEventListener listener : listeners) {
            try {
               listener.onWriteOutput(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.remove(listener);
            }
         }
      }
      
      @Override
      public void onSyntaxError(ProcessEventChannel channel, SyntaxErrorEvent event) throws Exception {
         for(ProcessEventListener listener : listeners) {
            try {
               listener.onSyntaxError(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.remove(listener);
            }
         }
      }
      
      @Override
      public void onBegin(ProcessEventChannel channel, BeginEvent event) throws Exception {
         for(ProcessEventListener listener : listeners) {
            try {
               listener.onBegin(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.remove(listener);
            }
         }
      }
      
      @Override
      public void onProfile(ProcessEventChannel channel, ProfileEvent event) throws Exception {
         for(ProcessEventListener listener : listeners) {
            try {
               listener.onProfile(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.remove(listener);
            }
         }
      }
      
      @Override
      public void onPong(ProcessEventChannel channel, PongEvent event) throws Exception {
         for(ProcessEventListener listener : listeners) {
            try {
               listener.onPong(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.remove(listener);
            }
         }
      }
      
      @Override
      public void onScope(ProcessEventChannel channel, ScopeEvent event) throws Exception {
         for(ProcessEventListener listener : listeners) {
            try {
               listener.onScope(channel, event);
            } catch(Exception e) {
               e.printStackTrace();
               listeners.remove(listener);
            }
         }
      }
   }
   
   private class ProcessAgentPinger implements Runnable {
   
      private final AtomicReference<String> location;
      private final Thread thread;
      private final long frequency;
      
      public ProcessAgentPinger(long frequency) {
         this.location = new AtomicReference<String>();
         this.thread = new Thread(this);
         this.frequency = frequency;
      }
      
      public void start(String address) {
         if(location.compareAndSet(null, address)) {
            configuration.setAddress(address);
            thread.start();
            
         }
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
      
      public boolean launch() {
         try {
            String address = location.get();

            if(address != null) {
               launcher.launch(configuration);
               return true;
            }
         }catch(Exception e) {
            e.printStackTrace();
         }
         return false;
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
               
               if(remaining > 0) {
                  launch(); // launch a new process at a time
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
