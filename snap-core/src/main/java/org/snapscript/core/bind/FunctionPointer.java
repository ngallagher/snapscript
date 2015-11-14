package org.snapscript.core.bind;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;

public class FunctionPointer {
   
   private final ArgumentConverter converter;
   private final Function function;
   private final Object[] arguments;
   
   public FunctionPointer(Function function, ArgumentConverter converter, Object[] arguments) {
      this.converter = converter;
      this.function = function;
      this.arguments = arguments;
   }
   
   public Result call(Scope scope, Object object) throws Exception{
      try {
         Object[] list = converter.convert(arguments);
         Invocation invocation = function.getInvocation();
         
         return invocation.invoke(scope, object, list); 
      } catch(Exception e) {
         throw new IllegalStateException("Invocation error for function '" + function + "'", e);
      }
   }
}
