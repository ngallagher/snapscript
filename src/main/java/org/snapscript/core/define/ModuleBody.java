package org.snapscript.core.define;

import org.snapscript.core.Scope;
import org.snapscript.core.execute.Result;
import org.snapscript.core.execute.ResultFlow;
import org.snapscript.core.execute.Statement;

public class ModuleBody extends Statement {

   private final Statement[] statements;
   
   public ModuleBody(Statement... statements) {
      this.statements = statements;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Result last = new Result();
      
      for(Statement statement : statements) {
         Result result = statement.compile(scope);
         ResultFlow type = result.getFlow();
         
         if(type != ResultFlow.NORMAL){
            return result;
         }
      }
      for(Statement statement : statements) {
         Result result = statement.execute(scope);
         ResultFlow type = result.getFlow();
         
         if(type != ResultFlow.NORMAL){
            return result;
         }
         last = result;
      }
      return last;
   }
}
