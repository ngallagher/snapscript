package org.snapscript.compile.instruction;

import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class ThrowStatement extends Statement {
   
   private final Evaluation evaluation;
   
   public ThrowStatement(Evaluation evaluation) {
      this.evaluation = evaluation; 
   }   

   @Override
   public Result execute(Scope scope) throws Exception {
      Value reference = evaluation.evaluate(scope, null);
      Object value = reference.getValue();
      
      return ResultType.getThrow(value);  
   }

}
