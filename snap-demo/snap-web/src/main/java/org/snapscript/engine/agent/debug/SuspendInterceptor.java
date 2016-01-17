package org.snapscript.engine.agent.debug;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.snapscript.compile.instruction.FunctionInvocation;
import org.snapscript.compile.instruction.construct.ConstructObject;
import org.snapscript.core.Scope;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.engine.event.ProcessEventChannel;
import org.snapscript.engine.event.ScopeEvent;

public class SuspendInterceptor implements TraceInterceptor {
   
   private static final List<Class> INSTRUCTIONS = Arrays.<Class>asList(FunctionInvocation.class, ConstructObject.class);
   
   private final ProcessEventChannel channel;
   private final ThreadProgressLocal monitor;
   private final BreakpointMatcher matcher;
   private final AtomicInteger counter;
   private final SuspendController latch;
   private final String process;
   
   public SuspendInterceptor(ProcessEventChannel channel, BreakpointMatcher matcher, SuspendController latch, String process) {
      this.monitor = new ThreadProgressLocal();
      this.counter = new AtomicInteger();
      this.matcher = matcher;
      this.channel = channel;
      this.process = process;
      this.latch = latch;
   }

   @Override
   public void before(Scope scope, Object instruction, String resource, int line, int key) {
      ThreadProgress step = monitor.get();
      Class type = instruction.getClass();

      if(matcher.match(resource, line) || step.suspend()) { 
         try {
            String thread = Thread.currentThread().getName();
            int count = counter.getAndIncrement();
            int depth = step.currentDepth();
            String path = ResourceExtractor.extractResource(resource);
            ScopeExtractor extractor = new ScopeExtractor(scope);
            ScopeEventBuilder builder = new ScopeEventBuilder(extractor, process, thread, type, path, line, depth, count);
            ScopeNotifier notifier = new ScopeNotifier(builder);
            ScopeEvent suspend = builder.suspendEvent();
            ScopeEvent resume = builder.resumeEvent();
            
            step.clear(); // clear config
            channel.send(suspend);
            notifier.start();
            suspend(notifier, extractor, resource, line);
            channel.send(resume);
         } catch(Exception e) {
            e.printStackTrace();
         }
      }
      if(INSTRUCTIONS.contains(type)) {
         step.increaseDepth();
      }
   }

   @Override
   public void after(Scope scope, Object instruction, String resource, int line, int key) {
      ThreadProgress step = monitor.get();
      Class type = instruction.getClass();
      
      if(INSTRUCTIONS.contains(type)) {
         step.reduceDepth();
      }
   }
   
   private void suspend(ScopeNotifier notifier, ScopeBrowser browser, String resource, int line) {
      ResumeType type = latch.suspend(notifier, browser);
      ThreadProgress step = monitor.get();
      
      step.resume(type);
   }
   
   private class ScopeNotifier extends Thread implements ResumeListener {
      
      private final ScopeEventBuilder builder;
      private final AtomicBoolean active;
      
      public ScopeNotifier(ScopeEventBuilder builder) {
         this.active = new AtomicBoolean(true);
         this.builder = builder;
      }

      @Override
      public void run() {
         try {
            while(active.get()) {
               Thread.sleep(1000);
               
               if(active.get()) {
                  ScopeEvent event = builder.suspendEvent();
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
