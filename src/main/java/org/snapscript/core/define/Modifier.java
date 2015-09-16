package org.snapscript.core.define;

import org.snapscript.core.Holder;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.execute.Evaluation;
import org.snapscript.parse.StringToken;

public class Modifier implements Evaluation {
   
   private final StringToken token;
   
   public Modifier(StringToken token) {
      this.token = token;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      String name = token.getValue();
      ModifierType modifier = ModifierType.resolveModifier(name);
      
      return new Holder(modifier);
   }

}