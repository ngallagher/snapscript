package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.SuperScope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

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
   public Value evaluate(Scope instance, Object left) throws Exception {
      if(left == null) {
         throw new IllegalArgumentException("Type required for super function call");
      }
      Type real = (Type)left;
      SuperScope s =new SuperScope(instance, real, type);
      InvocationDispatcher handler = dispatcher.dispatch(s, null);
      Value reference = function.evaluate(s, left);
      String name = reference.getString();      
      
      if(list != null) {
         Value array = list.evaluate(s, null); // arguments have no left hand side
         Object[] arguments = array.getValue();
         
         // XXX hack to pass up the type
         Object[] expand = new Object[arguments.length + 1];
         
         for(int i = 0; i < arguments.length; i++) {
            expand[i + 1] = arguments[i];
         }
         expand[0] = real;
         
         return handler.dispatch(name, expand);
      }
      return handler.dispatch(name, real);
   }
}