package org.snapscript.core.convert;

import static org.snapscript.core.convert.Score.EXACT;
import static org.snapscript.core.convert.Score.INVALID;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;

public class FunctionComparator {
   
   private final ConstraintMatcher matcher;
   
   public FunctionComparator(ConstraintMatcher matcher) {
      this.matcher = matcher;
   }
   
   public Score compare(Function actual, List<Function> require) throws Exception{
      String name = actual.getName();
      
      for(Function function : require) {
         String match = function.getName();
         
         if(name.equals(match)) {
            Score compare = compare(actual, function);
            
            if(compare != INVALID) {
               return compare;
            }
         }
      }
      return INVALID;
   }

   public Score compare(Function actual, Function require) throws Exception{
      Signature actualSignature = actual.getSignature();
      Signature requireSignature = require.getSignature();
      List<Type> actualTypes = actualSignature.getTypes();
      List<Type> constraintTypes = requireSignature.getTypes();
      int actualSize = actualTypes.size();
      int constraintSize = constraintTypes.size();
      
      if(actualSize == constraintSize) {
         return compare(actualTypes, constraintTypes);
      }
      return INVALID;
   }
   
   private Score compare(List<Type> actual, List<Type> require) throws Exception{
      int actualSize = actual.size();

      if(actualSize > 0) {
         Score total = INVALID;
         
         for(int i = 0; i < actualSize; i++) {
            Type actualType = actual.get(i);
            Type constraintType = require.get(i);
            ConstraintConverter converter = matcher.match(constraintType);
            Score score = converter.score(actualType);
            
            if(score.compareTo(INVALID) <= 0) { // must check for numbers
               return INVALID;
            }
            total = Score.sum(total, score); // sum for better match
         }
         return total;
      }
      return EXACT;
   }
}
