package org.snapscript.core.convert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.snapscript.core.Any;
import org.snapscript.core.Scope;

public class ProxyWrapper {

   private final ClassLoader loader;
   
   public ProxyWrapper() {
      this.loader = Any.class.getClassLoader();
   }
   
   public Object toProxy(Object object, Class... interfaces) {
      if(object != null) {
         if(Scope.class.isInstance(object)) {
            ProxyHandler handler = new ProxyHandler(this, (Scope)object);
            Class[] types = new Class[interfaces.length + 1];
            
            for(int i = 0; i < interfaces.length; i++) {
               types[i] = interfaces[i];
            }
            types[interfaces.length] = Any.class;
            
            return Proxy.newProxyInstance(loader, types, handler);
         }
      }
      return object;
   }
   
   public Object fromProxy(Object object) {
      if(object != null) {
         if(Proxy.class.isInstance(object)) {
            InvocationHandler handler = Proxy.getInvocationHandler(object);
           
            if(ProxyHandler.class.isInstance(handler)) {
               ProxyHandler proxy = (ProxyHandler)handler;
               Object value = proxy.extract();
               
               return value;
            }
         }
      }
      return object;
   }
}
