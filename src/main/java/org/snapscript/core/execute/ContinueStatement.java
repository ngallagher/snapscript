package org.snapscript.core.execute;

import org.snapscript.core.Scope;
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