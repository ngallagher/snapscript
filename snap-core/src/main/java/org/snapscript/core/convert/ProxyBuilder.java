package org.snapscript.core.convert;

import java.lang.reflect.Proxy;

import org.snapscript.core.Any;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ProxyBuilder {

   private final ClassLoader loader;
   private final Type type;
   
   public ProxyBuilder(Type type) {
      this.loader = Any.class.getClassLoader();
      this.type = type;
   }
   
   public Object create(Scope scope, Class... interfaces) {
      ProxyHandler handler = new ProxyHandler(scope, type);
      Class[] types = new Class[interfaces.length + 1];
      
      for(int i = 0; i < interfaces.length; i++) {
         types[i] = interfaces[i];
      }
      types[interfaces.length] = Any.class;
      
      return Proxy.newProxyInstance(loader, types, handler);
   }
}
