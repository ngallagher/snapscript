package org.snapscript.core.convert;

import org.snapscript.core.Type;

public class BooleanConverter extends ConstraintConverter {

   private final Type type;
   
   public BooleanConverter(Type type) {
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
      
      if(type == Boolean.class) {
         return EXACT;
      }
      if(type == String.class) {
         if(compatible(Boolean.class, value)) {
            return POSSIBLE;
         }
      }
      return INVALID;
   }
   
   @Override
   public Object convert(Object value) throws Exception {
      Class type = value.getClass();
      
      if(type == String.class) {
         return convert(Boolean.class, value);
      }
      if(type == Boolean.class) {
         Boolean number = (Boolean)value;
         return number.booleanValue();
      }
      throw new IllegalArgumentException("Conversion from " + type + " to boolean is not possible");
   }
}