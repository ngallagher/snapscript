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
         Result next = statement.execute(compound);
         ResultType type = next.getType();
         
         if (type.isReturn() || type.isThrow()) {
            return next;
         }
         if(type.isBreak()) {
            return ResultType.getNormal();
         }
      }
   }
}
