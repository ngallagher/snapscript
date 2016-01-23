package org.snapscript.compile.instruction.dispatch;

import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.bind.FunctionBinder;

public class ObjectDispatcher implements InvocationDispatcher {
   
   private final Object object;
   private final Scope scope;      
   
   public ObjectDispatcher(Scope scope, Object object) {
      this.object = object;
      this.scope = scope;
   }

   @Override
   public Value dispatch(String name, Object... arguments) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();
      Callable<Result> call = binder.bind(scope, object, name, arguments);
      Class type = object.getClass();
      
      if(call == null) {
         throw new IllegalStateException("Method '" + name + "' not found for " + type);
      }
      Result result = call.call();
      Object value = result.getValue();
      
      if(result.isThrow()) {
         throw new IllegalStateException("Method '" + name + "' for " + type + " had an exception");
      }
      return ValueType.getTransient(value);
   }
}