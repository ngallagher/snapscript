package org.snapscript.core.convert;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.execute.Result;

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
      Object[] list = converter.convert(arguments);
      Invocation invocation = function.getInvocation();

      return invocation.invoke(scope, object, list);
   }
}