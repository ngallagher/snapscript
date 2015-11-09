package org.snapscript.core.convert;

import org.snapscript.core.Scope;

public class ScopeConverter extends TypeConverter {
   
   @Override
   public int score(Object value) throws Exception {
      if(value != null) {
         if(Scope.class.isInstance(value)) {
            return EXACT;
         }
         return INVALID;
      }
      return POSSIBLE;
   }
   
   @Override
   public Object convert(Object object) {
      return object;
   }
}