package org.snapscript.core.convert;

import org.snapscript.core.Type;

public class CharacterConverter extends ConstraintConverter {

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
         if(compatible(Character.class, value)) {
            return POSSIBLE;
         }
      }
      return INVALID;
   }
   
   @Override
   public Object convert(Object value) throws Exception {
      Class type = value.getClass();
      
      if(type == String.class) {
         return convert(Character.class, value);
      }
      if(type == Character.class) {
         Character number = (Character)value;
         return number.charValue();
      }
      throw new IllegalArgumentException("Conversion from " + type + " to character is not possible");
   }
}