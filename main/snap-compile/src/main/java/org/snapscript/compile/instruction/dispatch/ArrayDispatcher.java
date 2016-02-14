package org.snapscript.compile.instruction.dispatch;

import java.util.List;
import java.util.concurrent.Callable;

import org.snapscript.compile.instruction.collection.ArrayConverter;
import org.snapscript.core.Context;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.bind.FunctionBinder;

public class ArrayDispatcher implements InvocationDispatcher {
   
   private final ArrayConverter converter;
   private final Object object;
   private final Scope scope;      
   
   public ArrayDispatcher(Scope scope, Object object) {
      this.converter = new ArrayConverter();
      this.object = object;
      this.scope = scope;
   }

   @Override
   public Value dispatch(String name, Object... arguments) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();
      List list = converter.convert(object);
      Callable<Result> call = binder.bind(scope, list, name, arguments);
      Class type = object.getClass();
      
      if(call == null) {
         throw new InternalStateException("Method '" + name + "' not found for " + type);
      }
      Result result = call.call();
      Object value = result.getValue();
      
      if(result.isThrow()) {
         throw new InternalStateException("Method '" + name + "' for " + type + " had an exception");
      }
      return ValueType.getTransient(value);
   }
}