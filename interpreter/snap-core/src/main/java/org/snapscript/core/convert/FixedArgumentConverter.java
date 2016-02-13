package org.snapscript.core.convert;

import static org.snapscript.core.convert.ConstraintConverter.EXACT;
import static org.snapscript.core.convert.ConstraintConverter.INVALID;

import org.snapscript.core.bind.ArgumentConverter;

public class FixedArgumentConverter implements ArgumentConverter { 

   private final ConstraintConverter[] converters;

   public FixedArgumentConverter(ConstraintConverter[] converters) {
      this.converters = converters;
   }
   
   public int score(Object... list) throws Exception {
      if(list.length != converters.length) {
         return INVALID;
      }
      if(list.length > 0) {
         int total = 0; 
      
         for(int i = 0; i < list.length; i++){
            ConstraintConverter converter = converters[i];
            Object value = list[i];
            int score = converter.score(value);
         
            if(score == 0) {
               return INVALID;
            }
            total += score;
            
         }
         return total;
      }
      return EXACT;
   }
   
   public Object[] convert(Object... list) throws Exception {
      if(list.length > 0) {
         Object[] result = new Object[list.length];
      
         for(int i = 0; i < list.length; i++){
            ConstraintConverter converter = converters[i];
            Object value = list[i];
            
            result[i] = converter.convert(value);
         }
         return result;
      }
      return list;
   }
}
