package org.snapscript.core.convert;

import static org.snapscript.core.convert.ConstraintConverter.EXACT;
import static org.snapscript.core.convert.ConstraintConverter.INVALID;

import org.snapscript.core.bind.ArgumentConverter;

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