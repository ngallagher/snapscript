package org.snapscript.core.convert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.snapscript.core.Any;
import org.snapscript.core.Scope;

public class ProxyWrapper {

   private final ProxyFactory factory;
   
   public ProxyWrapper() {
      this.factory = new ProxyFactory(this);
   }
   
   public Object toProxy(Object object) { 
      return toProxy(object, Any.class);
   }
   
   public Object toProxy(Object object, Class require) { 
      return toProxy(object, require, Any.class);
   }
   
   public Object toProxy(Object object, Class... require) { 
      if(object != null) {
         if(Scope.class.isInstance(object)) {
            Object proxy = factory.create(object);
            
            for(Class type : require) {
               if(!type.isInstance(proxy)) {
                  throw new IllegalStateException("Proxy does not implement " + require);
               }
            }
            return proxy;
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
