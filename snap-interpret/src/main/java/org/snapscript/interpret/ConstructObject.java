package org.snapscript.interpret;

import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Holder;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
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
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();
      Type qualifier = module.getType(name);
      
      if(qualifier == null) {
         throw new IllegalStateException("No type found for " + name); // class not found
      }
      if(list != null) {
         Value array = list.evaluate(scope, null); // arguments have no left hand side
         Object[] arguments = array.getValue();
         
         // XXX this is a hack for not to construct objects correctly...
         Object[] expand = new Object[arguments.length + 1];
         
         for(int i = 0; i < arguments.length; i++) {
            expand[i + 1] = arguments[i];
         }
         expand[0] = qualifier;
         
         if(arguments.length > 0) {
            Callable<Result> call = binder.bind(scope, qualifier, "new", expand);
           
            if(call == null){
               throw new IllegalStateException("No constructor for " + name);
            }
            Result result = call.call();
            Object instance = result.getValue();
            
            return new Holder(instance);
         }
      }
      Callable<Result> call = binder.bind(scope, qualifier, "new", qualifier);
      
      if(call == null){
         throw new IllegalStateException("No constructor for " + name);
      }
      Result result = call.call();
      Object instance = result.getValue();
      
      return new Holder(instance);
   }
}