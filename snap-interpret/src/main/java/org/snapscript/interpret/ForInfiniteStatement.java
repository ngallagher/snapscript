package org.snapscript.interpret;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class ForInfiniteStatement extends Statement {
   
   private final Statement statement;
   
   public ForInfiniteStatement(Statement statement) {
      this.statement = statement;
   }

   @Override
   public Result execute(Scope scope) throws Exception {
      Scope compound = scope.getScope();
      
      while(true) {
         Result next = statement.execute(compound);
         ResultFlow type = next.getFlow();
         
         if (type == ResultFlow.RETURN || type == ResultFlow.THROW) {
            return next;
         }
         if(type == ResultFlow.BREAK) {
            return new Result();
         }
      }
   }
}
