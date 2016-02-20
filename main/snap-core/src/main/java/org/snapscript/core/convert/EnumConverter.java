package org.snapscript.core.convert;

import org.snapscript.core.Type;

public class EnumConverter extends ConstraintConverter {
   
   private final Type type;
   
   public EnumConverter(Type type) {
      this.type = type;
   }
   
   @Override
   public int score(Type actual) throws Exception {
      Class real = actual.getType();
      Class require = type.getType();
      
      if(real != require) {
         Class parent = real.getSuperclass();
            
         if(parent == require) {
            return EXACT;
         }
         if(real == String.class) {
            return SIMILAR;
         }
         return INVALID;
      }
      return EXACT;
   }

   @Override
   public int score(Object value) throws Exception {
      if(value != null) {
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
      return POSSIBLE;
   }
   
   public Object convert(Object value) throws Exception {
      if(value != null) {
         Class actual = value.getClass();
         Class parent = actual.getSuperclass();
         
         if(parent == Enum.class) {
            return value;
         }
         String text = String.valueOf(value);
         String name = type.getName();
         Class require = Class.forName(name);
       
         return Enum.valueOf(require, text);
      }
      return null;
   }
}