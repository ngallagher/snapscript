package org.snapscript.compile.instruction;

import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
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
      Scope compound = scope.getInner(); 
      Result last = null;
      
      for(Statement statement : statements) {
         Result result = statement.compile(compound);
         ResultType type = result.getType();
         
         if(!type.isNormal()){
            return result;
         }
      }
      for(Statement statement : statements) {
         Result result = statement.execute(compound);
         ResultType type = result.getType();
         
         if(!type.isNormal()){
            return result;
         }
         last = result;
      }
      if(last == null) {
         return ResultType.getNormal();
      }
      return last;
   }
}