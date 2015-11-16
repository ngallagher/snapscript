package org.snapscript.compile.instruction;

public class CalculationOperand implements CalculationPart {   
   
   private final Evaluation evaluation;
   
   public CalculationOperand(Evaluation evaluation) {
      this.evaluation = evaluation;
   }
   
   @Override
   public Evaluation getEvaluation(){
      return evaluation;
   }
   
   @Override
   public NumericOperator getOperator(){
      return null;
   }
}