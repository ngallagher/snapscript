package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class AnnotationName implements Evaluation {

   private final TextLiteral literal;
   
   public AnnotationName(TextLiteral literal) {
      this.literal = literal;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      return literal.evaluate(scope, left);
   }
   
}
