package org.snapscript.core.error;

import org.snapscript.common.Stack;
import org.snapscript.core.Function;
import org.snapscript.core.Trace;

public class ThreadStack {
   
   private final StackTraceBuilder builder;
   private final ThreadLocalStack local;
   
   public ThreadStack() {
      this.builder = new StackTraceBuilder();
      this.local = new ThreadLocalStack();
   }
   
   public void update(Throwable cause) {
      Stack stack = local.get();
      StackTraceElement[] trace = builder.create(stack);
      
      cause.setStackTrace(trace);
   }
   
   public void before(Object trace) {
      Stack stack = local.get();
      
      if(trace != null) {
         stack.push(trace);
      }
   }
   
   public void after(Object trace) { // remove from stack
      Stack stack = local.get();
      int size = stack.size();
      
      while(!stack.isEmpty()) {
         Object next = stack.pop();
         
         if(next == trace) {
            break;
         }
      }
   }
   
   public void clear() {
      Stack stack = local.get();
      stack.clear();
   }

}
