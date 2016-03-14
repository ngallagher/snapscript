package org.snapscript.agent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.agent.event.PingEvent;
import org.snapscript.agent.event.PongEvent;
import org.snapscript.agent.event.ProcessEventChannel;

public class ConnectionChecker {

   private final AtomicBoolean active;
   private final AtomicLong update;
   private final HealthChecker checker;
   private final Thread thread;
   private final String process;
   private final String system;
   
   public ConnectionChecker(String process, String system) {
      this.checker = new HealthChecker(10000);
      this.thread = new Thread(checker);
      this.active = new AtomicBoolean();
      this.update = new AtomicLong();
      this.process = process;
      this.system = system;
   }
   
   public void update(ProcessEventChannel channel, PingEvent event, String resource) {
      PongEvent pong = new PongEvent(process, system,  resource, resource != null);
      
      try {
         long time = System.currentTimeMillis();
         
         update.set(time);
         channel.send(pong); // send a pong event
      } catch(Exception e) {
         e.printStackTrace();
         System.exit(0);
      }
   }
   
   public void start() {
      if(active.compareAndSet(false, true)) {
         thread.start();
      }
   }
   
   private class HealthChecker implements Runnable {
      
      private final long frequency;
      
      public HealthChecker(long frequency) {
         this.frequency = frequency;
      }
      
      @Override
      public void run() {
         try {
            while(true) {
               Thread.sleep(frequency);
               long last = update.get();
               long time = System.currentTimeMillis();
               long duration = time - last;
               
               if(duration > frequency) { // ensure pings are frequent
                  break;
               }
            }
         } catch(Exception e) {
            e.printStackTrace();
         } finally {
            System.exit(0);
         }
      }
   }

}
