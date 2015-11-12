package org.snapscript.core.convert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyExtractor {

   public ProxyExtractor() {
      super();
   }
   
   public Object extract(Object object) {
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
