package org.snapscript.core;

import org.snapscript.common.Stack;

@Bug("This does not work properly")
public class StackTraceBuilder {

   private final StackElementConverter converter;
   
   public StackTraceBuilder() {
      this.converter = new StackElementConverter();
   }
   
   public void update(Throwable cause, Stack stack) {
      Thread thread = Thread.currentThread();
      StackTraceElement[] list = thread.getStackTrace();
      int size = stack.size();
      int index = 0;
      
      for(int i = 0; i< size; i++) {
         Object value = stack.get(size-i);
         StackTraceElement element = converter.create(value);
      
         if(element != null) {
            list[index++] = element;
         }
      }
      cause.setStackTrace(list); // fill in the trace!!
   }
}
