package org.snapscript.compile.instruction;

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
      Result last = null;
      
      for(Statement statement : statements) {
         Result result = statement.compile(scope);
         
         if(!result.isNormal()){
            throw new IllegalStateException("Illegal statement");
         }
         last = result;
      }
      if(last == null) {
         return ResultType.getNormal();
      }
      return last;
   }
}
