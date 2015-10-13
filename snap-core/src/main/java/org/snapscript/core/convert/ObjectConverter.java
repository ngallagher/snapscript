package org.snapscript.core.convert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Holder;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.bind.FunctionBinder;

public class ObjectConverter extends TypeConverter {
   
   private final TypeExtractor extractor;
   private final Type type;
   
   public ObjectConverter(TypeExtractor extractor, Type type) {
      this.extractor = extractor;
      this.type = type;
   }

   @Override
   public int score(Object value) throws Exception {
      Type match = extractor.extract(value);
      
      if(match != null) {
         if(match.equals(type)) {
            return EXACT;
         }
         List<Type> types = match.getTypes();
         
         if(types.contains(type)) { // here we are checking the class hierarchy
            return SIMILAR;
         }
         return INVALID;
      }
      return EXACT;
   }
   
   @Override
   public Object convert(Object object) {
      Class require = type.getType();
      
      if(require != null && object != null) {
         Class actual = object.getClass();
      
         if(require.isInterface()) {
            if(Scope.class.isAssignableFrom(actual)) {
               return Proxy.newProxyInstance(require.getClassLoader(), 
                  new Class[]{require},  
                  new InvocationDelegate((Scope)object));
            }
         }
      }
      return object;
   }
   
   private class InvocationDelegate implements InvocationHandler {
      
      private final Object[] empty;
      private final Scope scope;
      
      public InvocationDelegate(Scope scope) {
         this.empty = new Object[]{};
         this.scope = scope;
      }
      
      @Override
      public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
         String name = method.getName();
         Module module = scope.getModule();
         Context context = module.getContext();
         FunctionBinder binder = context.getBinder();    
         Callable<Result> call = binder.bind(scope, scope, name, arguments == null ? empty : arguments); // here arguments can be null!!!
         
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
      
   }
}