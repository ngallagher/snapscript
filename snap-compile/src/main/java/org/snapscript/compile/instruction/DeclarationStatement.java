package org.snapscript.compile.instruction;

import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class DeclarationStatement extends Statement {

   private final Evaluation declaration;   
   
   public DeclarationStatement(Evaluation declaration) {
      this.declaration = declaration;     
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Value variable = declaration.evaluate(scope, null);              
      Object value = variable.getValue();      
      
      return new Result(ResultFlow.NORMAL, value);
   }
}