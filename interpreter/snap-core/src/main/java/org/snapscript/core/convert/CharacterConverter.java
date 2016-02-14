package org.snapscript.core.convert;

import org.snapscript.core.InternalArgumentException;
import org.snapscript.core.Type;

public class CharacterConverter extends ConstraintConverter {

   private final CharacterMatcher matcher;
   private final Type type;
   
   public CharacterConverter(Type type) {
      this.matcher = new CharacterMatcher();
      this.type = type;
   }
   
   @Override
   public int score(Object value) throws Exception {
      Class require = type.getType();
      
      if(value != null) {
         Class actual = value.getClass();
         
         if(actual == Character.class) {
            return EXACT;
         }
         if(actual == String.class) {
            String text = String.valueOf(value);
            
            if(matcher.matchCharacter(text)) {
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
         Class type = value.getClass();
         
         if(type == String.class) {
            return convert(require, (String)value);
         }
         if(type == Character.class) {
            return value;
         }
         throw new InternalArgumentException("Conversion from " + type + " to character is not possible");
      }
      if(require.isPrimitive()) {
         throw new InternalArgumentException("Invalid conversion from null to primitive character");
      }
      return null;
   }
}