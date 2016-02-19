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
      InternalError error = new InternalError(value);
      
      if(replace) {
         if(Throwable.class.isInstance(value)) {
            Throwable cause = (Throwable)value;
            stack.update(cause);
         }
         stack.update(error);
      }
      throw error;
   }
}
