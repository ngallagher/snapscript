package org.snapscript.interpret;

import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class CalculationResult implements Evaluation {

   private final Object value;

   public CalculationResult(Object value) {
      this.value = value;
   }

   public Value evaluate(Scope scope, Object left) throws Exception {
      return new Holder(value);
   }

}
