package org.snapscript.core;

import org.snapscript.common.ArrayStack;
import org.snapscript.common.Stack;

public class ThreadStack {
   
   private final StackTraceBuilder builder;
   private final StackTrace local;
   
   public ThreadStack() {
      this.builder = new StackTraceBuilder();
      this.local = new StackTrace();
   }
   
   public void update(Throwable cause) {
      Stack stack = local.get();
      builder.update(cause, stack);
   }
   
   public void invoke(Function function) {
      Stack stack = local.get();
      stack.push(function);
   }
   
   public void before(Trace trace) {
      Stack stack = local.get();
      stack.push(trace);
   }
   
   public void after(Trace trace) { // remove from stack
      Stack stack = local.get();
      int size = stack.size();
      
      while(!stack.isEmpty()) {
         Object next = stack.pop();
         
         if(next == trace) {
            break;
         }
      }
   }
   
   private static class StackTrace extends ThreadLocal<Stack> {
      
      public Stack initialValue() {
         return new ArrayStack();
      }
   }
}
