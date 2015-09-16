package org.snapscript.core.define;

import org.snapscript.core.Constant;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.execute.ArgumentList;
import org.snapscript.core.execute.Evaluation;

public class SuperFunction implements Evaluation {
   
   private final InvocationBinder dispatcher;
   private final ArgumentList list;
   private final Evaluation function;
   private final Type type;
   
   public SuperFunction(Evaluation function, Type type) {
      this(function, type, null);
   }
   
   public SuperFunction(Evaluation function, Type type, ArgumentList list) {
      this.dispatcher = new InvocationBinder();
      this.function = function;
      this.list = list;
      this.type = type;
   }
   
   @Override
   public Value evaluate(Scope x, Object left) throws Exception {
      TypeScope s =new TypeScope(x, type);
      Constant constant = new Constant(type);
      s.addConstant("class",constant);
      InvocationDispatcher handler = dispatcher.dispatch(s, left);
      Value reference = function.evaluate(s, left);
      String name = reference.getString();      
      
      if(list != null) {
         Value array = list.evaluate(s, null); // arguments have no left hand side
         Object[] arguments = array.getValue();
         
         return handler.dispatch(name, arguments);
      }
      return handler.dispatch(name);
   }
}