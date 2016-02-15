package org.snapscript.compile.instruction.define;

import org.snapscript.core.Bug;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

@Bug("name is wrong")
public class StaticBody extends Statement {

   private final Initializer initializer;
   private final Type type;
   
   public StaticBody(Initializer initializer, Type type) {
      this.initializer = initializer;
      this.type = type;
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      return initializer.compile(scope, type);
   }
}
