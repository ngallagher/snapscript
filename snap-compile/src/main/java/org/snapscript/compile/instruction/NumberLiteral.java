package org.snapscript.compile.instruction;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.NumberToken;
import org.snapscript.parse.StringToken;

public class NumberLiteral implements Evaluation {
   
   private final SignOperator operator;
   private final NumberToken token;

   public NumberLiteral(NumberToken token) {
      this(null, token);
   }
   
   public NumberLiteral(StringToken sign, NumberToken token) {
      this.operator = SignOperator.resolveOperator(sign);
      this.token = token;
   }

   public Value evaluate(Scope scope, Object left) throws Exception {
      Number number = token.getValue();
      
      if(number == null) {
         throw new IllegalStateException("Number value was null");
      }
      return operator.operate(number);
   }
}