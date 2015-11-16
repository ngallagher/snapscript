package org.snapscript.compile.instruction.condition;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class IfStatement extends Statement {
   
   private final Evaluation evaluation;
   private final Statement positive;
   private final Statement negative;
   
   public IfStatement(Evaluation evaluation, Statement positive) {
      this(evaluation, positive, null);
   }
   
   public IfStatement(Evaluation evaluation, Statement positive, Statement negative) {
      this.evaluation = evaluation;
      this.positive = positive;
      this.negative = negative;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Value result = evaluation.evaluate(scope, null);
      Boolean value = result.getBoolean();
      
      if(value.booleanValue()) {
         return positive.execute(scope);
      } else {
         if(negative != null) {
            return negative.execute(scope);
         }
      }            
      return new Result();
   }
}