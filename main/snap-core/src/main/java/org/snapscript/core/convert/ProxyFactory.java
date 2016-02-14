package org.snapscript.core.convert;

import java.lang.reflect.Proxy;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Scope;

public class ProxyFactory {

   private InterfaceCollector collector;
   private ProxyWrapper wrapper;
   private ClassLoader loader;
   
   public ProxyFactory(ProxyWrapper wrapper) {
      this.collector = new InterfaceCollector();
      this.wrapper = wrapper;
   }
   
   public Object create(Object value) {
      Class[] interfaces = collector.collect(value);
      
      if(interfaces.length > 0) {
         ProxyHandler handler = new ProxyHandler(wrapper, (Scope)value);
         
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
      return value;
   }
}
