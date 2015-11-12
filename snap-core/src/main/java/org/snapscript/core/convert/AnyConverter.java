package org.snapscript.core.convert;

import java.lang.reflect.Proxy;

import org.snapscript.core.Any;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class AnyConverter extends TypeConverter {
   
   private final Type type;
   
   public AnyConverter(Type type) {
      this.type = type;
   }
   
   @Override
   public int score(Object value) throws Exception {
      return EXACT;
   }
   
   public Object convert(Object object) {
      Class actual = object.getClass();
   
      if(Scope.class.isAssignableFrom(actual)) {
         return Proxy.newProxyInstance(Any.class.getClassLoader(), 
               new Class[]{Any.class},  
               new ProxyHandler((Scope)object, type));
      }
      return object;
   }
}
