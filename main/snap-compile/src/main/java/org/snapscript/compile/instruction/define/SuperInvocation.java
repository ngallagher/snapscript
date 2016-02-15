package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.dispatch.InvocationBinder;
import org.snapscript.compile.instruction.dispatch.InvocationDispatcher;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class SuperInvocation implements Evaluation {
   
   private final InvocationBinder dispatcher;
   private final SuperScopeBuilder builder;
   private final ArgumentList arguments;
   private final Evaluation function;
   
   public SuperInvocation(Evaluation function, ArgumentList arguments, Type type) {
      this.builder = new SuperScopeBuilder(type);
      this.dispatcher = new InvocationBinder();
      this.arguments = arguments;
      this.function = function;
   }
   
   @Override
   public Value evaluate(Scope instance, Object left) throws Exception {
      Scope scope = builder.create(instance, left);
      Type real = scope.getType();
      InvocationDispatcher handler = dispatcher.bind(scope, null);
      Value reference = function.evaluate(scope, left);
      String name = reference.getString();      
      
      if(arguments != null) {
         Value array = arguments.evaluate(scope, null); // arguments have no left hand side
         Object[] list = array.getValue();
         
         if(list.length > 0) {
            Object[] expand = new Object[list.length + 1];
         
            for(int i = 0; i < list.length; i++) {
               expand[i + 1] = list[i];
            }
            expand[0] = real;
         
            return handler.dispatch(name, expand);
         }
      }
      return handler.dispatch(name, real);
   }
   

}