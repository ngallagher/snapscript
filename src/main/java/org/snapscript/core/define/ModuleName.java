package org.snapscript.core.define;

import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.execute.Evaluation;
import org.snapscript.core.execute.TextLiteral;

public class ModuleName implements Evaluation {
   
   private final TextLiteral literal;
   
   public ModuleName(TextLiteral literal) {
      this.literal = literal;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      Value value = literal.evaluate(scope, left);
      String name = value.getValue();
      
      return new Holder(name);
   }  
}
