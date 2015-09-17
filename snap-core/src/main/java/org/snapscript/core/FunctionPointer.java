package org.snapscript.core;

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
