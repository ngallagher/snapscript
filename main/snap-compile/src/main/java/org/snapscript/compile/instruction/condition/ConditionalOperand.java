package org.snapscript.compile.instruction.condition;

import org.snapscript.compile.instruction.Evaluation;

public class ConditionalOperand implements ConditionalPart {
   
   private final Evaluation evaluation;
   
   public ConditionalOperand(Evaluation evaluation) {
      this.evaluation = evaluation;
   }
   
   @Override
   public Evaluation getEvaluation(){
      return evaluation;
   }
   
   @Override
   public CombinationOperator getOperator(){
      return null;
   }
}