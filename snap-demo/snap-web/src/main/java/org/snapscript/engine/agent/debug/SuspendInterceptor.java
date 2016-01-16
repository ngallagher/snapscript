package org.snapscript.engine.agent.debug;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.core.Value;
import org.snapscript.engine.event.ProcessEventChannel;
import org.snapscript.engine.event.ScopeEvent;

public class SuspendInterceptor implements TraceInterceptor {
   
   private final ProcessEventChannel channel;
   private final SuspendMatcher matcher;
   private final AtomicInteger counter;
   private final SuspendLatch latch;
   private final String process;
   
   public SuspendInterceptor(ProcessEventChannel channel, SuspendMatcher matcher, SuspendLatch latch, String process) {
      this.counter = new AtomicInteger();
      this.matcher = matcher;
      this.channel = channel;
      this.process = process;
      this.latch = latch;
   }

   @Override
   public void before(Scope scope, Object instruction, String resource, int line, int key) {
      if(matcher.match(resource, line)) { 
         try {
            String thread = Thread.currentThread().getName();
            State state = scope.getState();
            Set<String> names = state.getNames();
            int count = counter.getAndIncrement();
            
            if(!names.isEmpty()) {
               Map<String, String> variables = new HashMap<String, String>();
               ScopeEvent event = new ScopeEvent(process, thread, resource, line, count, variables);
               ScopeNotifier notifier = new ScopeNotifier(event);
               
               for(String name : names) {
                  Value value = state.getValue(name);
                  Object object = value.getValue();
                  String text = String.valueOf(object);
                  
                  variables.put(name, text);
               }
               channel.send(event);
               notifier.start();
               latch.suspend(notifier);
            }
         } catch(Exception e) {
            e.printStackTrace();
         }
      }
   }

   @Override
   public void after(Scope scope, Object instruction, String resource, int line, int key) {
      matcher.match(resource, line);
   }
   
   private class ScopeNotifier extends Thread implements ResumeListener {
      
      private final AtomicBoolean active;
      private final ScopeEvent event;
      
      public ScopeNotifier(ScopeEvent event) {
         this.active = new AtomicBoolean(true);
         this.event = event;
      }

      @Override
      public void run() {
         try {
            while(active.get()) {
               Thread.sleep(2000);
               channel.send(event);
            }
         } catch(Exception e) {
            e.printStackTrace();
         } finally {
            active.set(false);
         }
      }

      @Override
      public void resume(String thread) {
         active.set(false);
      }
      
      
   }

}
