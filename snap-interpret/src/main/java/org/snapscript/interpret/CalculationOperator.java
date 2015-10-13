package org.snapscript.interpret;

import org.snapscript.parse.StringToken;

public class CalculationOperator implements CalculationPart {   
   
   private final NumericOperator operator;
   
   public CalculationOperator(StringToken operator) {
      this.operator = NumericOperator.resolveOperator(operator);
   }
   
   public Evaluation getEvaluation(){
      return null;
   }
   
   public NumericOperator getOperator(){
      return operator;
   }
}