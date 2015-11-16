package org.snapscript.compile.instruction;

import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.parse.StringToken;

public class ContinueStatement extends Statement {
  
   private final StringToken token;
   
   public ContinueStatement(StringToken token){
      this.token = token;
   }      
   
   @Override
   public Result execute(Scope scope) throws Exception {
      return new Result(ResultFlow.CONTINUE);
   }
}