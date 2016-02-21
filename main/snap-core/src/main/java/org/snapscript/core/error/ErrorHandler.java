package org.snapscript.core.error;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;

public class ErrorHandler {

   private final ThreadStack stack;
   private final boolean replace;
   
   public ErrorHandler(ThreadStack stack) {
      this(stack, true);
   }
   
   public ErrorHandler(ThreadStack stack, boolean replace) {
      this.replace = replace;
      this.stack = stack;
   }
   
   public Result throwError(Scope scope, Object value) {
      if(InternalError.class.isInstance(value)) {
         throw (InternalError)value;
      }
      InternalError error = new InternalError(value);
      
      if(replace) {
         StackTraceElement[] list = stack.build();
         
         if(Throwable.class.isInstance(value)) {
            Throwable cause = (Throwable)value;
            StackTraceElement[] trace = stack.build(cause);
            
            cause.setStackTrace(trace);
            error.setStackTrace(trace);
         } else {
            error.setStackTrace(list); // when there is no cause
         }
      }
      throw error;
   }
}
