package org.snapscript.compile.instruction.dispatch;

import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.bind.FunctionBinder;

public class ScopeDispatcher implements InvocationDispatcher {
   
   private final Scope object;
   private final Scope scope;      
   
   public ScopeDispatcher(Scope scope, Object object) {
      this.object = (Scope)object;
      this.scope = scope;
   }

   @Override
   public Value dispatch(String name, Object... arguments) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();
      Callable<Result> local = binder.bind(scope, object, name, arguments);
      
      if(local == null) {
         Callable<Result> external = binder.bind(scope, module, name, arguments);
         
         if(external != null) {
            Result result = external.call();
            Object data = result.getValue();
            
            if(result.isThrow()) {
               throw new InternalStateException("Method '" + name + "' for module '" + module + "' had an exception");
            }
            return ValueType.getTransient(data);   
         }
      }
      Type type = object.getType();
      
      if(local == null) {
         throw new InternalStateException("Method '" + name + "' not found for type '" + type + "'");
      }
      Result result = local.call();
      Object data = result.getValue();
      
      if(result.isThrow()) {
         throw new InternalStateException("Method '" + name + "' for type '" + type + "' had an exception");
      }
      return ValueType.getTransient(data);           
   }
}