package org.snapscript.core.execute;

import static org.snapscript.core.execute.ResultFlow.NORMAL;

import org.snapscript.core.Scope;
import org.snapscript.parse.StringToken;

public class NoOperation extends Statement {
   
   private final StringToken token;
   
   public NoOperation() {
      this(null);
   }
   
   public NoOperation(StringToken token) {
      this.token = token;
   }

   @Override
   public Result execute(Scope scope) throws Exception {
      return NORMAL.result;
   }

}