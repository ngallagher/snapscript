package org.snapscript.compile.instruction;

import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public class ReturnStatement extends Statement {
   
   private final Evaluation evaluation;
   private final StringToken token;
   
   public ReturnStatement(StringToken token){
      this(null, token);
   }
   
   public ReturnStatement(Evaluation evaluation){
      this(evaluation, null);
   }
   
   public ReturnStatement(Evaluation evaluation, StringToken token){
      this.evaluation = evaluation;
      this.token = token;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      if(evaluation != null) {
         Value value = evaluation.evaluate(scope, null);
         Object object = value.getValue();
         
         return ResultType.getReturn(object);
      }
      return ResultType.getReturn();
   }
}