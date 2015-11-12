package org.snapscript.core.convert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyArgumentExtractor {

   public ProxyArgumentExtractor() {
      super();
   }
   
   public Object[] extract(Object[] arguments) {
      if(arguments != null) {
         Object[] convert = new Object[arguments.length];
         
         for(int i = 0; i < arguments.length; i++) {
            Object object = arguments[i];
         
            if(Proxy.class.isInstance(object)) {
               InvocationHandler handler = Proxy.getInvocationHandler(object);
              
               if(ProxyHandler.class.isInstance(handler)) {
                  ProxyHandler proxy = (ProxyHandler)handler;
                  Object value = proxy.extract();
                  
                  convert[i] = value;
               }
            } else {
               convert[i] = object;
            }
         }
         return convert;
      }
      return new Object[]{};
   }
}
