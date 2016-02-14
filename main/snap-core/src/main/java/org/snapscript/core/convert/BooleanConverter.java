package org.snapscript.core.convert;

import org.snapscript.core.InternalArgumentException;
import org.snapscript.core.Type;

public class BooleanConverter extends ConstraintConverter {

   private final BooleanMatcher matcher;
   private final Type type;
   
   public BooleanConverter(Type type) {
      this.matcher = new BooleanMatcher();
      this.type = type;
   }
   
   @Override
   public int score(Object value) throws Exception {
      Class require = type.getType();
      
      if(value != null) {
         Class actual = value.getClass();
         
         if(actual == Boolean.class) {
            return EXACT;
         }
         if(actual == String.class) {
            String text = String.valueOf(value);
            
            if(matcher.matchBoolean(text)) {
               return POSSIBLE;
            }
         }
         return INVALID;
      }
      if(require.isPrimitive()) {
         return INVALID;
      }
      return POSSIBLE;
   }
   
   @Override
   public Object convert(Object value) throws Exception {
      Class require = type.getType();
      
      if(value != null) {
         Class actual = value.getClass();
         
         if(actual == String.class) {
            return convert(require, (String)value);
         }
         if(actual == Boolean.class) {
            return value;
         }
         throw new InternalArgumentException("Conversion from " + actual + " to boolean is not possible");
      }
      if(require.isPrimitive()) {
         throw new InternalArgumentException("Invalid conversion from null to primitive boolean");
      }
      return null;
   }
}