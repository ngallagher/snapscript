package org.snapscript.compile.instruction;

import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public class NullLiteral implements Evaluation {
   
   private final StringToken token;

   public NullLiteral() {
      this(null);
   }
   
   public NullLiteral(StringToken token) {
      this.token = token;
   }

   public Value evaluate(Scope scope, Object left) throws Exception {
      return new Holder(null);
   }
}