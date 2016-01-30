package org.snapscript.compile.instruction;

import org.snapscript.core.Context;
import org.snapscript.core.ExpressionExecutor;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ExpressionEvaluation implements Evaluation {
   
   private final Evaluation evaluation;
   
   public ExpressionEvaluation(Evaluation evaluation) {
      this.evaluation = evaluation;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value value = evaluation.evaluate(scope, left);
      String source = value.getString();
      Module module = scope.getModule();
      Context context = module.getContext();
      ExpressionExecutor executor = context.getExecutor();
      Object result = executor.execute(scope, source);
      
      return ValueType.getTransient(result);
   }

}
