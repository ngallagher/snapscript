package org.snapscript.interpret;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public class SignedNumber implements Evaluation {
   
   private SignOperator operator;
   private NumberLiteral literal;
   private Value result;
   
   public SignedNumber(NumberLiteral literal) {
      this(null, literal);
   }
   
   public SignedNumber(StringToken sign, NumberLiteral literal) {
      this.operator = SignOperator.resolveOperator(sign);
      this.literal = literal;
   }

   public Value evaluate(Scope scope, Object left) throws Exception {
      if(result == null) {
         Value value = literal.evaluate(scope, null);
         Number number = value.getValue();
         
         if(number == null) {
            throw new IllegalStateException("Number value was null");
         }
         result = operator.operate(number);
      }
      return result;
   }
}