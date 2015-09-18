package org.snapscript.interpret;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;


public class Delegate implements Evaluation {
   
   private final Evaluation evaluation;
   
   public Delegate(Evaluation evaluation) {
      this.evaluation = evaluation;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      return evaluation.evaluate(scope, left);
   }
}