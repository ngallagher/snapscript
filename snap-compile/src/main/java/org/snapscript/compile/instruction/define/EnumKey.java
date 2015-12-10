package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.TextLiteral;
import org.snapscript.core.Transient;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class EnumKey implements Evaluation {
   
   protected final TextLiteral literal;
   
   public EnumKey(TextLiteral literal) {
      this.literal = literal;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      Value value = literal.evaluate(scope, left);
      String name = value.getValue();

      return new Transient(name);
   }  
}
