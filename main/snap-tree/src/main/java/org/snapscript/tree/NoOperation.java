package org.snapscript.tree;

import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
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
      return ResultType.getNormal();
   }

}
