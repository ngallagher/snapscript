package org.snapscript.core.exception;

import org.snapscript.core.Scope;

public class CauseExtractor {

   public CauseExtractor() {
      super();
   }
   
   public Object extract(Scope scope, Object cause) {
      if(StackTraceException.class.isInstance(cause)) {
         StackTraceException error = (StackTraceException)cause;
         return error.getOriginal();
      }
      return cause;
   }
}
