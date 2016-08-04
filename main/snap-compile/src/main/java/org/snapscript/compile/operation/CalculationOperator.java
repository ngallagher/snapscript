package org.snapscript.compile.operation;

import org.snapscript.core.Evaluation;
import org.snapscript.parse.StringToken;

public class CalculationOperator implements CalculationPart {   
   
   private final NumericOperator operator;
   
   public CalculationOperator(StringToken operator) {
      this.operator = NumericOperator.resolveOperator(operator);
   }
   
   @Override
   public Evaluation getEvaluation(){
      return null;
   }
   
   @Override
   public NumericOperator getOperator(){
      return operator;
   }
}