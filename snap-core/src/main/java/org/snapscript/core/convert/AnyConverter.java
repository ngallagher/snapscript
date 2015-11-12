package org.snapscript.core.convert;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class AnyConverter extends TypeConverter {
   
   private final ProxyBuilder builder;
   private final Type type;
   
   public AnyConverter(Type type) {
      this.builder = new ProxyBuilder(type);
      this.type = type;
   }
   
   @Override
   public int score(Object value) throws Exception {
      return EXACT;
   }
   
   public Object convert(Object object) {
      Class actual = object.getClass();
   
      if(Scope.class.isAssignableFrom(actual)) {
         return builder.create((Scope)object);
      }
      return object;
   }
}
