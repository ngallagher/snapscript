package org.snapscript.core;

public class InternalArgumentException extends InternalException {
   
   public InternalArgumentException(String message) {
      super(message);
   }
   
   public InternalArgumentException(String message, Throwable cause) {
      super(message, cause);
   }
}
