package org.snapscript.interpret;

import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class ExpressionStatement extends Statement {
   
   private final Evaluation expression;

   public ExpressionStatement(Evaluation expression) {
      this.expression = expression;
   }

   @Override
   public Result execute(Scope scope) throws Exception {
      Value reference = expression.evaluate(scope, null);
      Object value = reference.getValue();
      
      return new Result(ResultFlow.NORMAL, value);
   }
}