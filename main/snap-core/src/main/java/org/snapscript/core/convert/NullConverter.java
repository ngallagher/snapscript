package org.snapscript.core.convert;

import org.snapscript.core.Type;

public class NullConverter extends ConstraintConverter {

   @Override
   public int score(Type type) throws Exception {
      return SIMILAR;
   }
   
   @Override
   public int score(Object value) throws Exception {
      return SIMILAR;
   }
   
   public Object convert(Object object) {
      return object;
   }
}