package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class InstanceInitializer extends Initializer {
   
   private final Evaluation evaluation;
   
   public InstanceInitializer(Evaluation evaluation){
      this.evaluation = evaluation;
   }

   @Override
   public Result execute(Scope instance, Type type) throws Exception {
      if(evaluation != null) {
         evaluation.evaluate(instance, null);
      }
      return ResultType.getNormal();
   }
}