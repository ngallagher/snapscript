package org.snapscript.assemble;

import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class Script extends Statement {
   
   private final Statement[] statements;
   
   public Script(Statement... statements) {
      this.statements = statements;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Result last = new Result();
      
      for(Statement statement : statements) {
         Result result = statement.compile(scope);
         ResultFlow type = result.getFlow();
         
         if(type != ResultFlow.NORMAL){
            throw new IllegalStateException("Illegal statement");
         }
      }
      for(Statement statement : statements) {
         Result result = statement.execute(scope);
         ResultFlow type = result.getFlow();
         
         if(type == ResultFlow.THROW) {
            throw new IllegalStateException("Exception not caught");
         }
         if(type != ResultFlow.NORMAL){
            throw new IllegalStateException("Illegal statement");
         }
         last = result;
      }
      return last;
   }
}