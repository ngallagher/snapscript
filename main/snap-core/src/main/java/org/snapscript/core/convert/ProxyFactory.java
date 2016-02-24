package org.snapscript.core.convert;

import java.lang.reflect.Proxy;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Scope;

public class ProxyFactory {

   private InterfaceCollector collector;
   private ProxyWrapper wrapper;
   private ClassLoader loader;
   private Context context;
   
   public ProxyFactory(ProxyWrapper wrapper, Context context) {
      this.collector = new InterfaceCollector();
      this.wrapper = wrapper;
      this.context = context;
   }
   
   public Object create(Scope scope, Class... require) {
      Class[] interfaces = collector.collect(scope);
      
      if(interfaces.length > 0) {
         ScopeProxyHandler handler = new ScopeProxyHandler(wrapper, scope);
         
         if(loader == null) {
            Thread thread = Thread.currentThread();
            ClassLoader context = thread.getContextClassLoader();
            
            if(context == null) {
               throw new InternalStateException("Thread context class loader was null");
            }
            loader = context;
         }
         return Proxy.newProxyInstance(loader, interfaces, handler);
      }
      return scope;
   }
   
   public Object create(Function function, Class... require) {
      Class[] interfaces = collector.filter(require);
      
      if(interfaces.length > 0) {
         FunctionProxyHandler handler = new FunctionProxyHandler(wrapper, context, function);
         
         if(loader == null) {
            Thread thread = Thread.currentThread();
            ClassLoader context = thread.getContextClassLoader();
            
            if(context == null) {
               throw new InternalStateException("Thread context class loader was null");
            }
            loader = context;
         }
         return Proxy.newProxyInstance(loader, interfaces, handler);
      }
      return function;
   }
}
