package org.snapscript.core.convert;

import org.snapscript.core.Type;

public class StringConverter extends ConstraintConverter {
   
   @Override
   public int score(Type type) throws Exception {
      Class real = type.getType();
      
      if(real == String.class) {
         return EXACT;
      }
      return POSSIBLE;
   }
   
   @Override
   public int score(Object value) throws Exception {
      if(value != null) {
         Class type = value.getClass();
      
         if(type == String.class) {
            return EXACT;
         }
      }
      return POSSIBLE;
   }
   
   public Object convert(Object object) throws Exception {
      return String.valueOf(object);
   }
}