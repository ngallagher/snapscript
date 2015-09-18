package org.snapscript.interpret;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class Choice implements Evaluation {
   
   private final Evaluation evaluation;
   private final Evaluation positive;
   private final Evaluation negative;
   
   public Choice(Evaluation evaluation, Evaluation positive, Evaluation negative) {
      this.evaluation = evaluation;
      this.positive = positive;
      this.negative = negative;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value result = evaluation.evaluate(scope, null);
      Boolean value = result.getBoolean();
      
      if(value.booleanValue()) {
         return positive.evaluate(scope, left);
      } 
      return negative.evaluate(scope, left);
   }
}
