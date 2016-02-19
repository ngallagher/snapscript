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
import org.snapscript.core.ValueTypeExtractor;
import org.snapscript.core.bind.FunctionBinder;

public class ScopeDispatcher implements InvocationDispatcher {
   
   private final ValueTypeExtractor extractor;
   private final Scope object;
   private final Scope scope;      
   
   public ScopeDispatcher(Scope scope, Object object) {
      this.extractor = new ValueTypeExtractor();
      this.object = (Scope)object;
      this.scope = scope;
   }

   @Override
   public Value dispatch(String name, Object... arguments) throws Exception {
      Context context = scope.getContext();
      FunctionBinder binder = context.getBinder();
      Callable<Result> local = binder.bind(scope, object, name, arguments);
      
      if(local == null) {
         Module module = scope.getModule();
         Callable<Result> external = binder.bind(scope, module, name, arguments);
         
         if(external != null) {
            Result result = external.call();
            Object data = result.getValue();
            
            return ValueType.getTransient(data);   
         }
      }
      if(local == null) {
         Type type = extractor.extract(scope, object);
         Module module = type.getModule();
         
         throw new InternalStateException("Method '" + name + "' not found for " + module + "." + type);
      }
      Result result = local.call();
      Object data = result.getValue();
      
      return ValueType.getTransient(data);           
   }
}