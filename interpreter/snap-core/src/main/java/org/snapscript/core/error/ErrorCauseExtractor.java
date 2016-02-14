package org.snapscript.core.error;

import org.snapscript.core.Scope;

public class ErrorCauseExtractor {

   public ErrorCauseExtractor() {
      super();
   }
   
   public Object extract(Scope scope, Object cause) {
      if(Error.class.isInstance(cause)) {
         Error error = (Error)cause;
         return error.getOriginal();
      }
      return cause;
   }
}
