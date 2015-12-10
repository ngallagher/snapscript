package org.snapscript.compile.instruction.operation;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Transient;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.Token;

public class PostfixDecrement implements Evaluation {
   
   private final Evaluation evaluation;
   private final Token operator;
   
   public PostfixDecrement(Evaluation evaluation, Token operator) {
      this.evaluation = evaluation;
      this.operator = operator;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception { // this is rubbish
      Value reference = evaluation.evaluate(scope, left);
      Double value = reference.getDouble();
      
      reference.setValue(value - 1);
      return new Transient(value);
   }
}