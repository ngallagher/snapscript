package org.snapscript.core.execute;

import org.snapscript.parse.StringToken;

public class ConditionalOperator implements ConditionalPart {   
   
   private final CombinationOperator operator;
   
   public ConditionalOperator(StringToken operator) {
      this.operator = CombinationOperator.resolveOperator(operator);
   }
   
   @Override
   public Evaluation getEvaluation(){
      return null;
   }
  
   @Override
   public CombinationOperator getOperator(){
      return operator;
   }
}