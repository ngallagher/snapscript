package org.snapscript.compile.instruction;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class Script extends Statement {
   
   private final Statement[] statements;
   
   public Script(Statement... statements) {
      this.statements = statements;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Result last = null;
      
      for(Statement statement : statements) {
         Result result = statement.compile(scope);
         
         if(!result.isNormal()){
            throw new InternalStateException("Illegal statement");
         }
      }
      return last;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Result last = null;
      
      for(Statement statement : statements) {
         Result result = statement.execute(scope);
         
         if(result.isThrow()) {
            throw new InternalStateException("Exception not caught");
         }
         if(!result.isNormal()){
            throw new InternalStateException("Illegal statement");
         }
         last = result;
      }
      return last;
   }
}