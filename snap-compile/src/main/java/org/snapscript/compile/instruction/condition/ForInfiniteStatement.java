package org.snapscript.compile.instruction.condition;

import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class ForInfiniteStatement extends Statement {
   
   private final Statement statement;
   
   public ForInfiniteStatement(Statement statement) {
      this.statement = statement;
   }

   @Override
   public Result execute(Scope scope) throws Exception {
      Scope compound = scope.getInner();
      
      while(true) {
         Result result = statement.execute(compound);
         
         if(result.isReturn() || result.isThrow()) {
            return result;
         }
         if(result.isBreak()) {
            return ResultType.getNormal();
         }
      }
   }
}
