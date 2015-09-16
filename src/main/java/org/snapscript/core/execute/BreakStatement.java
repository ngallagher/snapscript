package org.snapscript.core.execute;

import org.snapscript.core.Scope;
import org.snapscript.parse.StringToken;

public class BreakStatement extends Statement {
   
   private final StringToken token;
   
   public BreakStatement(StringToken token){
      this.token = token;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      return new Result(ResultFlow.BREAK);
   }
}