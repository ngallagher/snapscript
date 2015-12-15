package org.snapscript.compile.instruction.condition;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class WhileStatement extends Statement {
   
   private final Evaluation evaluation;
   private final Statement statement;
   
   public WhileStatement(Evaluation evaluation, Statement statement) {
      this.evaluation = evaluation;
      this.statement = statement;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      while(true) {
         Value result = evaluation.evaluate(scope, null);
         Boolean value = result.getBoolean();
         
         if(value.booleanValue()) {
            Result next = statement.execute(scope);
            ResultType type = next.getType();
            
            if(type.isReturn()) {
               return next;
            }
            if(type.isBreak()) {
               return ResultType.getNormal();
            }
         } else {
            return ResultType.getNormal();
         } 
      }
   }
}