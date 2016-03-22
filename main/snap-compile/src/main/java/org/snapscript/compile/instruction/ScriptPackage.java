package org.snapscript.compile.instruction;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class ScriptPackage extends Statement {

   private final Statement[] statements;
   
   public ScriptPackage(Statement... statements) {
      this.statements = statements;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Result last = ResultType.getNormal();
      
      for(Statement statement : statements) {
         Result result = statement.compile(scope);
         
         if(!result.isNormal()){
            throw new InternalStateException("Illegal statement");
         }
         last = result;
      }
      return last;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Result last = ResultType.getNormal();
      
      for(Statement statement : statements) {
         Result result = statement.execute(scope);
         
         if(!result.isNormal()){
            throw new InternalStateException("Illegal statement");
         }
         last = result;
      }
      return last;
   }
}
