package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.define.InvocationBinder;
import org.snapscript.compile.instruction.define.InvocationDispatcher;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class FunctionInvocation implements Evaluation {
   
   private final InvocationBinder dispatcher;
   private final ArgumentList list;
   private final Evaluation function;
   
   public FunctionInvocation(Evaluation function) {
      this(function, null);
   }
   
   public FunctionInvocation(Evaluation function, ArgumentList list) {
      this.dispatcher = new InvocationBinder();
      this.function = function;
      this.list = list;
   }
   
   public Value evaluate(Scope scope, Object left) throws Exception {
      InvocationDispatcher handler = dispatcher.dispatch(scope, left);
      Value reference = function.evaluate(scope, left);
      String name = reference.getString();      
      
      if(list != null) {
         Value array = list.evaluate(scope, null); // arguments have no left hand side
         Object[] arguments = array.getValue();
         
         return handler.dispatch(name, arguments);
      }
      return handler.dispatch(name);
      /*
      if(left != null) {
         Class require = left.getClass();
         Type type = scope.getType(require);

         if(list != null) {
            Value array = list.evaluate(scope, null); // arguments have no left hand side
            Object[] arguments = array.getValue();
            
            if(arguments.length > 0) {
               Type[] parameters = new Type[arguments.length];
               
               for(int i = 0; i < arguments.length; i++){
                  Object argument = arguments[i];
                  
                  if(argument != null) {
                     parameters[i] = scope.getType(argument.getClass());
                  }
               }               
               Function function = FunctionBinder.INSTANCE.match(type, name, parameters);
               Invocation invocation = function.getInvocation();
               Object result = invocation.invoke(scope, left, arguments);
               
               return new Constant(result);
            }
         }
         Function function = FunctionBinder.INSTANCE.match(type, name);
         Invocation invocation = function.getInvocation();
         Object result = invocation.invoke(scope, left);
         
         return new Constant(result);
      } 
      Function value = scope.getFunction(name);
      Invocation invocation = value.getInvocation();
      
      if(list != null) {
         Value array = list.evaluate(scope, null); // arguments have no left hand side
         Object[] arguments = array.getValue();
         Object result = invocation.invoke(scope, null, arguments);
        
         return new Constant(result);
      }
      Object ret = invocation.invoke(scope, null);      
      
      return new Constant(ret);*/
      
   }
}