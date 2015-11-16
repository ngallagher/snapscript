package org.snapscript.compile.instruction.define;

import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class InitializerStatement extends Statement {

   private final Initializer initializer;
   private final Type type;
   
   public InitializerStatement(Initializer initializer, Type type) {
      this.initializer = initializer;
      this.type = type;
   }

   @Override
   public Result execute(Scope scope) throws Exception {
      return initializer.execute(scope, type);
   }
}
