package org.snapscript.core.convert;

import org.snapscript.core.Type;

public class NullConverter extends ConstraintConverter { // XXX FunctionMatcher matches abstract methods!!

   @Override
   public int score(Type type) throws Exception {
      return EXACT; // this should be SIMILAR
   }
   
   @Override
   public int score(Object value) throws Exception {
      return EXACT; // this should be SIMILAR
   }
   
   public Object convert(Object object) {
      return object;
   }
}