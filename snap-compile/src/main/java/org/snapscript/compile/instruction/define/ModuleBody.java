package org.snapscript.compile.instruction.define;

import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class ModuleBody extends Statement {

   private final Statement[] statements;
   
   public ModuleBody(Statement... statements) {
      this.statements = statements;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Result last = null;
      
      for(Statement statement : statements) {
         Result result = statement.compile(scope);
         ResultType type = result.getType();
         
         if(!type.isNormal()){
            return result;
         }
      }
      for(Statement statement : statements) {
         Result result = statement.execute(scope);
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
