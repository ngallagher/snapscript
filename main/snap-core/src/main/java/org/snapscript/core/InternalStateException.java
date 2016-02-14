package org.snapscript.core;

public class InternalStateException extends InternalException {
   
   public InternalStateException(String message) {
      super(message);
   }
   
   public InternalStateException(String message, Throwable cause) {
      super(message, cause);
   }
}
