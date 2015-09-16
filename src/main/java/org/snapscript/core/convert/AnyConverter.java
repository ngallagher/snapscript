package org.snapscript.core.convert;

public class AnyConverter extends TypeConverter {
   
   @Override
   public int score(Object value) throws Exception {
      return EXACT;
   }
   
   public Object convert(Object object) {
      return object;
   }
}
