package org.snapscript.core.bind;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.error.ThreadStack;

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
      Context context = scope.getContext();
      Object[] list = converter.convert(arguments);
      Invocation invocation = function.getInvocation();
      ThreadStack stack = context.getStack();
      
      try {
         stack.before(function);
         return invocation.invoke(scope, object, list);
      } finally {
         stack.after(function);
      }
   }
}
