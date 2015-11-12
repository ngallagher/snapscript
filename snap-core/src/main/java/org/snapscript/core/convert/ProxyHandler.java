package org.snapscript.core.convert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.bind.FunctionBinder;

public class ProxyHandler implements InvocationHandler {
   
   private final ProxyArgumentExtractor extractor;
   private final Scope scope;
   private final Type type;
   
   public ProxyHandler(Scope scope, Type type) {
      this.extractor = new ProxyArgumentExtractor();
      this.scope = scope;
      this.type = type;
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
         throw new IllegalStateException("Method '" + name + "' not found for type '" + type + "'");
      }
      Result result = call.call();
      ResultFlow flow = result.getFlow();
      Object data = result.getValue();
      
      if(flow == ResultFlow.THROW) {
         throw new IllegalStateException("Method '" + name + "' for type '" + type + "' had an exception");
      }
      return data;   
   }
   
   public Scope extract() {
      return scope;
   }
   
}
