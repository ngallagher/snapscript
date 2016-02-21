package org.snapscript.compile.instruction.dispatch;

import java.util.List;
import java.util.concurrent.Callable;

import org.snapscript.compile.instruction.collection.ArrayConverter;
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

public class ArrayDispatcher implements InvocationDispatcher {
   
   private final ValueTypeExtractor extractor;
   private final ArrayConverter converter;
   private final Object object;
   private final Scope scope;      
   
   public ArrayDispatcher(ValueTypeExtractor extractor, Scope scope, Object object) {
      this.converter = new ArrayConverter();
      this.extractor = extractor;
      this.object = object;
      this.scope = scope;
   }

   @Override
   public Value dispatch(String name, Object... arguments) throws Exception {
      Context context = scope.getContext();
      FunctionBinder binder = context.getBinder();
      List list = converter.convert(object);
      Callable<Result> call = binder.bind(scope, list, name, arguments);
      
      if(call == null) {
         Type type = extractor.extract(scope, object);
         Module module = type.getModule();
         
         throw new InternalStateException("Method '" + name + "' not found for " + module + "." + type + "[]");
      }
      Result result = call.call();
      Object value = result.getValue();

      return ValueType.getTransient(value);
   }
}