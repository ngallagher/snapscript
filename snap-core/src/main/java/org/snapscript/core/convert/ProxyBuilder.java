package org.snapscript.core.convert;

import java.lang.reflect.Proxy;

import org.snapscript.core.Any;
import org.snapscript.core.Scope;

public class ProxyBuilder {

   private final ClassLoader loader;
   
   public ProxyBuilder() {
      this.loader = Any.class.getClassLoader();
   }
   
   public Object create(Object object, Class... interfaces) {
      if(object != null) {
         if(Scope.class.isInstance(object)) {
            ProxyHandler handler = new ProxyHandler((Scope)object);
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
}
