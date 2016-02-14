package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.dispatch.InvocationBinder;
import org.snapscript.compile.instruction.dispatch.InvocationDispatcher;
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
      InvocationDispatcher handler = dispatcher.bind(scope, left);
      Value reference = function.evaluate(scope, left);
      String name = reference.getString();      
      
      if(list != null) {
         Value array = list.evaluate(scope, null); // arguments have no left hand side
         Object[] arguments = array.getValue();
         
         return handler.dispatch(name, arguments);
      }
      return handler.dispatch(name); 
   }
}