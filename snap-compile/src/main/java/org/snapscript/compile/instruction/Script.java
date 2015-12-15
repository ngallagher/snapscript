package org.snapscript.compile.instruction;

import org.snapscript.core.Bug;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class Script extends Statement {
   
   private final Statement[] statements;
   
   public Script(Statement... statements) {
      this.statements = statements;
   }
   
   @Bug("TODO why do we need to compile every time??? declarations go missing???")
   @Override
   public Result execute(Scope scope) throws Exception {
      Result last = null;
      
      for(Statement statement : statements) {
         Result result = statement.compile(scope);
         
         if(!result.isNormal()){
            throw new IllegalStateException("Illegal statement");
         }
      }
      for(Statement statement : statements) {
         Result result = statement.execute(scope);
         
         if(result.isThrow()) {
            throw new IllegalStateException("Exception not caught");
         }
         if(!result.isNormal()){
            throw new IllegalStateException("Illegal statement");
         }
         last = result;
      }
      return last;
   }
}