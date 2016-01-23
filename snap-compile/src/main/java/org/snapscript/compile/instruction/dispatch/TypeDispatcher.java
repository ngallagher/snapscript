package org.snapscript.compile.instruction.dispatch;

import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.bind.FunctionBinder;

public class TypeDispatcher implements InvocationDispatcher {
   
   private final ObjectDispatcher handler;
   private final Scope scope;      
   private final Type type;
   
   public TypeDispatcher(Scope scope, Object object) {
      this.handler = new ObjectDispatcher(scope, object);
      this.type = (Type)object;
      this.scope = scope;
   }

   @Override
   public Value dispatch(String name, Object... arguments) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();    
      Callable<Result> call = binder.bind(scope, type, name, arguments);
      
      if(call == null) {
         return handler.dispatch(name, arguments);
      }
      Result result = call.call();
      Object data = result.getValue();
      
      if(result.isThrow()) {
         throw new IllegalStateException("Method '" + name + "' for type '" + type + "' had an exception");
      }
      return ValueType.getTransient(data);           
   } 
}