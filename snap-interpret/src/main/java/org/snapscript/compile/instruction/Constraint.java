package org.snapscript.compile.instruction;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class Constraint {
   
   private final TextLiteral token;
   
   public Constraint(TextLiteral token) {
      this.token = token;
   }
   
   public Value evaluate(Scope scope, Object left) throws Exception {
      return token.evaluate(scope, left);
   }
}
