package org.snapscript.core.bind;

import org.snapscript.core.ArgumentConverter;
import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.error.ThreadStack;

public class FunctionPointer {
   
   private final ThreadStack stack;
   private final Function function;
   private final Object[] arguments;
   
   public FunctionPointer(Function function, ThreadStack stack, Object[] arguments) {
      this.arguments = arguments;
      this.function = function;
      this.stack = stack;
   }
   
   public Result call(Scope scope, Object object) throws Exception{
      Signature signature = function.getSignature();
      ArgumentConverter converter = signature.getConverter();
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
