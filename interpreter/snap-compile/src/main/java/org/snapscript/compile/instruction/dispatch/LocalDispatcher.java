package org.snapscript.compile.instruction.dispatch;

import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.bind.FunctionBinder;

public class LocalDispatcher implements InvocationDispatcher {
   
   private final Scope scope;      
   
   public LocalDispatcher(Scope scope) {
      this.scope = scope;
   }

   @Override
   public Value dispatch(String name, Object... arguments) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();
      Callable<Result> call = binder.bind(scope, module, name, arguments);
      
      if(call == null) {
         throw new IllegalStateException("Method '" + name + "' not found in scope");
      }
      Result result = call.call();
      Object value = result.getValue();
      
      if(result.isThrow()) {
         throw new IllegalStateException("Method '" + name + "' had an exception");
      }
      return ValueType.getTransient(value);  
   }
   
}