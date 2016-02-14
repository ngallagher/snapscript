package org.snapscript.core.convert;

import static org.snapscript.core.convert.ConstraintConverter.*;

import org.snapscript.core.bind.ArgumentConverter;

public class VariableArgumentConverter implements ArgumentConverter { 
   
   private final ConstraintConverter[] converters;

   public VariableArgumentConverter(ConstraintConverter[] converters) {
      this.converters = converters;
   }
   
   public int score(Object... list) throws Exception {
      if(list.length > 0) {
         int require = converters.length;
         int start = require - 1;
         int remaining = list.length - start;
         int total = 0;
         
         if(remaining < 0) {
            return INVALID;
         }
         for(int i = 0; i < start; i++){
            ConstraintConverter converter = converters[i];
            Object value = list[i];
            int score = converter.score(value);
            
            if(score == 0) {
               return INVALID;
            }
            total += score;
         }
         if (remaining > 0) {
            for (int i = 0; i < remaining; i++) {
               ConstraintConverter converter = converters[require - 1];
               Object value = list[i + start];
               int score = converter.score(value);
               
               if(score == 0) {
                  return INVALID;
               }
               total += score;
            }
         }
         return total;
      }
      if(converters.length == 1) {
         return EXACT;
      }
      return INVALID;
      
   }

   public Object[] convert(Object... list) throws Exception {
      if(list.length > 0) {
         int require = converters.length;
         int start = require - 1;
         int remaining = list.length - start;
         
         for(int i = 0; i < start; i++){
            ConstraintConverter converter = converters[i];
            Object value = list[i];
            
            list[i] = converter.convert(value);
         }
         if (remaining > 0) {
            for (int i = 0; i < remaining; i++) {
               ConstraintConverter converter = converters[require - 1];
               Object value = list[i + start];
               
               list[i + start] = converter.convert(value);
            }
         }
      }
      return list;
   }
}
