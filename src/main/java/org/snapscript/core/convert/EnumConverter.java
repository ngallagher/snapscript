package org.snapscript.core.convert;

import org.snapscript.core.Type;

public class EnumConverter extends TypeConverter {
   
   private final TypeExtractor extractor;
   private final Type type;
   
   public EnumConverter(TypeExtractor extractor, Type type) {
      this.extractor = extractor;
      this.type = type;
   }

   @Override
   public int score(Object value) throws Exception {
      Class type = value.getClass();
      Class parent = type.getSuperclass();
      
      if(parent == Enum.class) {
         return EXACT;
      }
      if(type == String.class) {
         return SIMILAR;
      }
      return INVALID;
   }
   
   public Object convert(Object object) throws Exception {
      Class actual = object.getClass();
      Class parent = actual.getSuperclass();
      
      if(parent == Enum.class) {
         return object;
      }
      String text = String.valueOf(object);
      String name = type.getName();
      Class require = Class.forName(name);
    
      return Enum.valueOf(require, text);
   }
}