package org.snapscript.core;


public class ScopeConverter extends TypeConverter {
   
   @Override
   public int score(Object value) throws Exception {
      if(value instanceof Scope) {
         return EXACT;
      }
      return INVALID;
   }
   
   @Override
   public Object convert(Object object) {
      return object;
   }
}