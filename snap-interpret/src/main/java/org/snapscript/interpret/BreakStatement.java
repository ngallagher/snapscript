package org.snapscript.interpret;

import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
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