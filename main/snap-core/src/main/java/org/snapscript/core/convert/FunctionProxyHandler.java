package org.snapscript.core.convert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Result;
import org.snapscript.core.Transient;
import org.snapscript.core.Value;
import org.snapscript.core.bind.FunctionBinder;

public class FunctionProxyHandler implements InvocationHandler {
   
   private final ProxyArgumentExtractor extractor;
   private final Function function;
   private final Context context;
   private final Value value;
   
   public FunctionProxyHandler(ProxyWrapper wrapper, Context context, Function function) {
      this.extractor = new ProxyArgumentExtractor(wrapper);
      this.value = new Transient(function);
      this.function = function;
      this.context = context;
   }
   
   @Override
   public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
      FunctionBinder binder = context.getBinder();  
      Object[] convert = extractor.extract(arguments);
      Callable<Result> call = binder.bind(value, convert); // here arguments can be null!!!
      
      if(call == null) {
         throw new InternalStateException("Closure not matched");
      }
      Result result = call.call();
      Object data = result.getValue();
      
      return data;   
   }
   
   public Function extract() {
      return function;
   }
   
}
