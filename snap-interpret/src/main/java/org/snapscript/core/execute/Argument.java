package org.snapscript.core.execute;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class Argument implements Evaluation{
   
   private final Evaluation evaluation;
   
   public Argument(Evaluation evaluation){
      this.evaluation = evaluation;
   }
   
   public Value evaluate(Scope scope, Object left) throws Exception {
      return evaluation.evaluate(scope, left);
   }
}