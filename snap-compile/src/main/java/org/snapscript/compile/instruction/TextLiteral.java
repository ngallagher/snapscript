package org.snapscript.compile.instruction;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.parse.StringToken;

public class TextLiteral implements Evaluation {
   
   private final StringToken token;

   public TextLiteral(StringToken token) {
      this.token = token;
   }

   public Value evaluate(Scope scope, Object left) throws Exception {
      String text = token.getValue();
      
      if(text == null) {
         throw new IllegalStateException("Text value was null");
      }
      return ValueType.getTransient(text);
   }
}