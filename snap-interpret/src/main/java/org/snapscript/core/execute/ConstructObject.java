package org.snapscript.core.execute;

import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.FunctionBinder;
import org.snapscript.core.Holder;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

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
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();
      Type qualifier = module.getType(name);
      
      if(list != null) {
         Value array = list.evaluate(scope, null); // arguments have no left hand side
         Object[] arguments = array.getValue();
         
         if(arguments.length > 0) {
            Callable<Result> call = binder.bind(scope, qualifier, "new", arguments);
           
            if(call == null){
               throw new IllegalStateException("No constructor for " + name);
            }
            Result result = call.call();
            Object instance = result.getValue();
            
            return new Holder(instance);
         }
      }
      Callable<Result> call = binder.bind(scope, qualifier, "new");
      
      if(call == null){
         throw new IllegalStateException("No constructor for " + name);
      }
      Result result = call.call();
      Object instance = result.getValue();
      
      return new Holder(instance);
   }
}