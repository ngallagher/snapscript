package org.snapscript.core.bind;

import static org.snapscript.core.convert.Score.INVALID;

import org.snapscript.core.ArgumentConverter;
import org.snapscript.core.Function;
import org.snapscript.core.Signature;
import org.snapscript.core.Value;
import org.snapscript.core.convert.Score;
import org.snapscript.core.error.ThreadStack;

public class ValueFunctionMatcher {
   
   private final ThreadStack stack;
   
   public ValueFunctionMatcher(ThreadStack stack) {
      this.stack = stack;
   }
   
   public FunctionPointer match(Value value, Object... values) throws Exception { // match function variable
      Object object = value.getValue();
      
      if(Function.class.isInstance(object)) {
         Function function = (Function)object;
         Signature signature = function.getSignature();
         ArgumentConverter match = signature.getConverter();
         Score score = match.score(values);
         
         if(score.compareTo(INVALID) > 0) {
            return new FunctionPointer(function, stack, values); 
         }
      }
      return null;
   }
}
