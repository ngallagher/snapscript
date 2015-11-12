package org.snapscript.core.convert;

import org.snapscript.core.Scope;

public class AnyConverter extends TypeConverter {
   
   private final ProxyBuilder builder;
   
   public AnyConverter() {
      this.builder = new ProxyBuilder();
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
