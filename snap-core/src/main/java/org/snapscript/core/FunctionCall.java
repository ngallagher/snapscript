package org.snapscript.core;

import java.util.concurrent.Callable;

public class FunctionCall implements Callable<Result> {
   
   private final FunctionPointer pointer;
   private final Object source;
   private final Scope scope;
   private final String name;
   
   public FunctionCall(FunctionPointer pointer, Scope scope, Object source, String name) {
      this.pointer = pointer;
      this.source = source;
      this.scope = scope;
      this.name = name;
   }
   
   @Override
   public Result call() throws Exception {
      try {
         return pointer.call(scope, source);
      } catch(Exception e) {
         throw new IllegalStateException("Invocation error for function " + name, e);
      }
   }
}
