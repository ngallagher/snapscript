package org.snapscript.compile.instruction.operation;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class CalculationList implements Evaluation { 
   
   private CalculationPart[] parts;   
   private Evaluation evaluation;

   public CalculationList(CalculationPart... parts) {
      this.parts = parts;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      if(evaluation == null) {
         Calculator calculator = new Calculator();
         
         for(CalculationPart part : parts) {
            calculator.update(part);
         }
         evaluation = calculator.create();
      }
      return evaluation.evaluate(scope, left);
   }   
}