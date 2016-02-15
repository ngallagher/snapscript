package org.snapscript.compile.instruction.operation;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;

public class CalculationList implements Compilation { 
   
   private CalculationPart[] parts; 

   public CalculationList(CalculationPart... parts) {
      this.parts = parts;
   }
   
   @Override
   public Evaluation compile(Context context, String resource, int line) throws Exception {
      Calculator calculator = new Calculator();
      
      for(CalculationPart part : parts) {
         calculator.update(part);
      }
      return calculator.create();
   }
}