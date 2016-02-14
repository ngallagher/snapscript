package org.snapscript.core.exception;

import org.snapscript.core.Trace;

public class StackTraceException extends RuntimeException {
   
   private final Object original;
   private final String stack;
   private final String chain;
   private final Trace origin;
   
   public StackTraceException(Object original, Trace origin, String stack) {
      this(original, origin, stack, null);
   }
   
   public StackTraceException(Object original, Trace origin, String stack, String chain) {
      this.original = original;
      this.origin = origin;
      this.stack = stack;
      this.chain = chain;
   }
   
   public String getDescription() {
      String stack = getStack();
      
      if(chain != null) {
         return stack + "\n" + chain;
      }
      return stack;
   }

   public String getStack() {
      return stack;
   }
   
   public Object getOriginal() {
      return original;
   }
   
   public String getChain() {
      return chain;
   }
   
   public Trace getOrigin() {
      return origin;
   }
   
   @Override
   public String toString() {
      return String.valueOf(original);
   }
}
