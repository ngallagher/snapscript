package org.snapscript.core.error;

import org.snapscript.core.Trace;

public class InternalError extends RuntimeError {
   
   private final Object original;
   private final String stack;
   private final String chain;
   private final Trace origin;
   
   public InternalError(Object original, Trace origin, String stack) {
      this(original, origin, stack, null);
   }
   
   public InternalError(Object original, Trace origin, String stack, String chain) {
      this.original = original;
      this.origin = origin;
      this.stack = stack;
      this.chain = chain;
   }
   
   @Override
   public String getDescription() {
      String stack = getStack();
      
      if(chain != null) {
         return stack + "\n" + chain;
      }
      return stack;
   }

   @Override
   public String getStack() {
      return stack;
   }
   
   @Override
   public Object getOriginal() {
      return original;
   }
   
   @Override
   public String getChain() {
      return chain;
   }
   
   @Override
   public Trace getOrigin() {
      return origin;
   }
   
   @Override
   public String toString() {
      return String.valueOf(original);
   }
}
