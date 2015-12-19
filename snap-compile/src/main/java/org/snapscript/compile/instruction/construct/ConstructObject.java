package org.snapscript.compile.instruction.construct;

import java.util.concurrent.Callable;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.TextLiteral;
import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.bind.FunctionBinder;

public class ConstructObject implements Evaluation {
   
   private final ArgumentList list;
   private final TextLiteral type;
   
   public ConstructObject(TextLiteral type) {
      this(type, null);         
   }
   
   public ConstructObject(TextLiteral type, ArgumentList list) {
      this.type = type;
      this.list = list;
   }      
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception { // this is rubbish
      Value value = type.evaluate(scope, null);
      String name = value.getString();
      Module module = scope.getModule();
      Type type = module.getType(name);
      
      if(type == null) {
         throw new IllegalStateException("No type found for " + name + " in '" + module + "'"); // class not found
      }
      Callable<Result> call = bind(scope, type);
           
      if(call == null){
         throw new IllegalStateException("No constructor for " + name);
      }
      Result result = call.call();
      Object instance = result.getValue();
      
      return ValueType.getTransient(instance);
   }
   
   private Callable<Result> bind(Scope scope, Type type) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();
      Class real = type.getType();
      
      if(list != null) {
         Value array = list.evaluate(scope, null); // arguments have no left hand side
         Object[] arguments = array.getValue();

         if(real == null) {
            Object[] expand = new Object[arguments.length + 1];
            
            for(int i = 0; i < arguments.length; i++) {
               expand[i + 1] = arguments[i];
            }
            expand[0] = type;
            
            return binder.bind(scope, type, "new", expand);
         }
         return binder.bind(scope, type, "new", arguments);
      }
      if(real == null) {
         return binder.bind(scope, type, "new", type);
      }
      return binder.bind(scope, type, "new");
   }
}