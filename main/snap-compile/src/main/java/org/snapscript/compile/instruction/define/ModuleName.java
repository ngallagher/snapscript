package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ModuleName implements Evaluation {
   
   private final TextLiteral literal;
   
   public ModuleName(TextLiteral literal) {
      this.literal = literal;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      Value value = literal.evaluate(scope, left);
      String name = value.getValue();
      
      return ValueType.getTransient(name);
   }  
}
