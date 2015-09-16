package org.snapscript.core.execute;

import org.snapscript.core.Scope;

public class TryFinallyStatement extends Statement {
   
   private final Statement statement;
   private final Statement finish;
   
   public TryFinallyStatement(Statement statement, Statement finish) {
      this.statement = statement;
      this.finish = finish;
   }    

   @Override
   public Result execute(Scope scope) throws Exception {
      try {
         return statement.execute(scope);
      } finally {
         finish.execute(scope);         
      }
   }

}