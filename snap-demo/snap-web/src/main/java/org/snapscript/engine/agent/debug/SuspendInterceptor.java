package org.snapscript.engine.agent.debug;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.snapscript.compile.instruction.FunctionInvocation;
import org.snapscript.compile.instruction.construct.ConstructObject;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.core.Value;
import org.snapscript.engine.event.ProcessEventChannel;
import org.snapscript.engine.event.ScopeEvent;

public class SuspendInterceptor implements TraceInterceptor {
   
   private static final List<Class> INSTRUCTIONS = Arrays.<Class>asList(FunctionInvocation.class, ConstructObject.class);
   
   private final ProcessEventChannel channel;
   private final ThreadStepLocal monitor;
   private final BreakpointMatcher matcher;
   private final AtomicInteger counter;
   private final SuspendLatch latch;
   private final String process;
   
   public SuspendInterceptor(ProcessEventChannel channel, BreakpointMatcher matcher, SuspendLatch latch, String process) {
      this.monitor = new ThreadStepLocal();
      this.counter = new AtomicInteger();
      this.matcher = matcher;
      this.channel = channel;
      this.process = process;
      this.latch = latch;
   }

   @Override
   public void before(Scope scope, Object instruction, String resource, int line, int key) {
      ThreadStep step = monitor.get();
      Class type = instruction.getClass();
      
      if(INSTRUCTIONS.contains(type)) {
         step.increaseDepth();
      }
      if(matcher.match(resource, line) || step.suspend()) { 
         try {
            String thread = Thread.currentThread().getName();
            String origin = type.getSimpleName();
            State state = scope.getState();
            Set<String> names = state.getNames();
            int count = counter.getAndIncrement();
            String path = ResourceExtractor.extractResource(resource);
            Map<String, String> variables = new HashMap<String, String>();
            ScopeEvent event = new ScopeEvent(process, thread, origin, path, line, count, variables);
            ScopeNotifier notifier = new ScopeNotifier(event);
            
            for(String name : names) {
               Value value = state.getValue(name);
               Object object = value.getValue();
               String text = String.valueOf(object);
               
               variables.put(name, text);
            }
            step.clear(); // clear config
            channel.send(event);
            notifier.start();
            suspend(notifier, resource, line);
         } catch(Exception e) {
            e.printStackTrace();
         }
      }
   }

   @Override
   public void after(Scope scope, Object instruction, String resource, int line, int key) {
      ThreadStep step = monitor.get();
      Class type = instruction.getClass();
      
      if(INSTRUCTIONS.contains(type)) {
         step.reduceDepth();
      }
   }
   
   private void suspend(ScopeNotifier notifier, String resource, int line) {
      ResumeType type = latch.suspend(notifier);
      ThreadStep step = monitor.get();
      
      step.resume(type);
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
               if(active.get()) {
                  channel.send(event);
               }
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
