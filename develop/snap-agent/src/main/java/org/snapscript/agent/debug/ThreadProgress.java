package org.snapscript.agent.debug;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.TraceType;

public class ThreadProgress {

   private final AtomicReference<ResumeType> resume;
   private final AtomicInteger match;
   private final AtomicInteger depth;
   
   public ThreadProgress() {
      this.resume = new AtomicReference<ResumeType>();
      this.match = new AtomicInteger();
      this.depth = new AtomicInteger();
   }
   
   public int currentDepth() {
      return depth.get();
   }
   
   public void beforeInstruction(TraceType type) {
      if(type == TraceType.CONSTRUCT) {
         depth.getAndIncrement();
      } else if(type == TraceType.INVOKE) {
         depth.getAndIncrement();
      }
   }
   
   public void afterInstruction(TraceType type) {
      if(type == TraceType.CONSTRUCT) {
         depth.getAndDecrement();
      } else if(type == TraceType.INVOKE) {
         depth.getAndDecrement();
      }
   }
   
   public void resume(ResumeType type) {
      int value = depth.get();
      
      if(type == ResumeType.RUN) {
         match.set(-1);
      } else if(type == ResumeType.STEP_IN) {
         match.set(value + 1);
      } else if(type == ResumeType.STEP_OUT) {
         match.set(value - 1);
      } else if(type == ResumeType.STEP_OVER) {
         match.set(value);
      }  
      resume.set(type);
   }
   
   public boolean suspend() {
      ResumeType type = resume.get();
      int require = match.get();
      int actual = depth.get();
      
      if(type != null) {
         if(type == ResumeType.RUN) {
            return false;
         } else if(type == ResumeType.STEP_IN) {
            return true; // always step in
         } else if(type == ResumeType.STEP_OUT) {
            return actual <= require;
         } else if(type == ResumeType.STEP_OVER) {
            return actual <= require;
         } 
         return require == actual;
      }
      return false;
   }
   
   public void clear() {
      resume.set(null);
      match.set(-1);
   }
}
