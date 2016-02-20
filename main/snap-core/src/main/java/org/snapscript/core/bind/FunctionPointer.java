package org.snapscript.core.bind;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.error.ThreadStack;

public class FunctionPointer {
   
   private final ArgumentConverter converter;
   private final ThreadStack stack;
   private final Function function;
   private final Object[] arguments;
   
   public FunctionPointer(Function function, ArgumentConverter converter, ThreadStack stack, Object[] arguments) {
      this.converter = converter;
      this.function = function;
      this.arguments = arguments;
      this.stack = stack;
   }
   
   public Result call(Scope scope, Object object) throws Exception{
      Object[] list = converter.convert(arguments);
      Invocation invocation = function.getInvocation();
      
      try {
         stack.before(function);
         return invocation.invoke(scope, object, list);
      } finally {
         stack.after(function);
      }
   }
}
