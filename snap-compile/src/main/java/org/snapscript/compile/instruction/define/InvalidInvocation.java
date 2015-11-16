package org.snapscript.compile.instruction.define;

import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;

public class InvalidInvocation implements Invocation {
   
   private final Signature signature;
   private final Statement statement;
   
   public InvalidInvocation(Statement statement, Signature signature) {
      this.statement = statement;
      this.signature = signature;
   }
   
   @Override
   public Result invoke(Scope scope, Object object, Object... list) throws Exception {
      throw new IllegalStateException("Function is abstract");
   }
}
