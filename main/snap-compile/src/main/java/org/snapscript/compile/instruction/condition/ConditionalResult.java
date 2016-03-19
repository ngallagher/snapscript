package org.snapscript.compile.instruction.condition;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class ConditionalResult implements Evaluation {
   
   private final Evaluation evaluation;
   
   public ConditionalResult(ConditionalOperand operand) {
      this.evaluation = operand.getEvaluation();
   }
  
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      return evaluation.evaluate(scope, left);
   }

}
