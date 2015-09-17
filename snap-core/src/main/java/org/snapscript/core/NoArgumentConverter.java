package org.snapscript.core;

import static org.snapscript.core.TypeConverter.EXACT;
import static org.snapscript.core.TypeConverter.INVALID;

public class NoArgumentConverter implements ArgumentConverter {

   @Override
   public int score(Object... list) throws Exception {
      if(list.length == 0) {
         return EXACT;
      }
      return INVALID;
   }

   @Override
   public Object[] convert(Object... list) throws Exception {
      return list;
   }

}
