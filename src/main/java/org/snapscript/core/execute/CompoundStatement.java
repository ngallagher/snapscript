package org.snapscript.core.execute;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Scope;
import org.snapscript.parse.StringToken;

public class CompoundStatement extends Statement {
   
   private final Statement[] statements;

   public CompoundStatement(StringToken token) {
      this();
   }
   
   public CompoundStatement(Statement... statements) {
      this.statements = statements;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Scope compound = new CompoundScope(scope);
      Result last = new Result();
      
      for(Statement statement : statements) {
         Result result = statement.compile(compound);
         ResultFlow type = result.getFlow();
         
         if(type != ResultFlow.NORMAL){
            return result;
         }
      }
      for(Statement statement : statements) {
         Result result = statement.execute(compound);
         ResultFlow type = result.getFlow();
         
         if(type != ResultFlow.NORMAL){
            return result;
         }
         last = result;
      }
      return last;
   }
}