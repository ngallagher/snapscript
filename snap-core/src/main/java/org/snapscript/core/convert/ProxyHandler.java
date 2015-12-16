package org.snapscript.core.convert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.bind.FunctionBinder;

public class ProxyHandler implements InvocationHandler {
   
   private final ProxyArgumentExtractor extractor;
   private final Scope scope;
   
   public ProxyHandler(Scope scope) {
      this.extractor = new ProxyArgumentExtractor();
      this.scope = scope;
   }
   
   @Override
   public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
      String name = method.getName();
      Module module = scope.getModule();
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();  
      Object[] convert = extractor.extract(arguments);
      Callable<Result> call = binder.bind(scope, scope, name, convert); // here arguments can be null!!!
      
      if(call == null) {
         throw new IllegalStateException("Method '" + name + "' not found");
      }
      Result result = call.call();
      Object data = result.getValue();
      
      if(result.isThrow()) {
         throw new IllegalStateException("Method '" + name + "' had an exception");
      }
      return data;   
   }
   
   public Scope extract() {
      return scope;
   }
   
}
