package org.snapscript.core.execute;

import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class CalculationList implements Evaluation { // maybe this should be optimistic?? On exception then check stuff!!
   
   private final AtomicReference<Evaluation> cache;
   private final CalculationPart[] parts;   

   public CalculationList(CalculationPart... parts) {
      this.cache = new AtomicReference<Evaluation>();
      this.parts = parts;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Evaluation evaluation = cache.get();
      
      if(evaluation == null) {
         Calculator calculator = new Calculator();
         
         for(CalculationPart part : parts) {
            calculator.update(part);
         }
         evaluation = calculator.create();
         
         if(evaluation != null) {
            cache.set(evaluation);
         } 
      }
      return evaluation.evaluate(scope, left);
   }   
}