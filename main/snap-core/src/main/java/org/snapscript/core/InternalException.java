package org.snapscript.core;

public class InternalException extends RuntimeException {
   
   public InternalException(String message) {
      super(message);
   }
   
   public InternalException(Throwable cause) {
      super(cause);
   }
   
   public InternalException(String message, Throwable cause) {
      super(message, cause);
   }
}
