package org.snapscript.core.convert;

import org.snapscript.core.Type;

public class CharacterConverter extends TypeConverter {

   private final Type type;
   
   public CharacterConverter(Type type) {
      this.type = type;
   }
   
   @Override
   public int score(Object value) throws Exception {
      Class actual = type.getType();
      
      if(value != null) {
         return match(value);
      }
      if(actual.isPrimitive()) {
         return INVALID;
      }
      return POSSIBLE;
   }
   
   private int match(Object value) throws Exception {
      Class type = value.getClass();
      
      if(type == Character.class) {
         return EXACT;
      }
      if(type == String.class) {
         return POSSIBLE;
      }
      return INVALID;
   }
   
   @Override
   public Object convert(Object value) throws Exception {
      Class type = value.getClass();
      
      if(type == String.class) {
         String text = String.valueOf(value);
         return convert(Byte.class, text);
      }
      if(type == Character.class) {
         Character number = (Character)value;
         return number.charValue();
      }
      throw new IllegalArgumentException("Conversion from " + type + " to byte is not possible");
   }
}