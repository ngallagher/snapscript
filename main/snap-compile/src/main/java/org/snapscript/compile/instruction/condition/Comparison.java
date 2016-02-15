package org.snapscript.compile.instruction.condition;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public class Comparison implements Evaluation {   
   
   private final RelationalOperator operator;
   private final Evaluation left;
   private final Evaluation right;
   
   public Comparison(Evaluation left) {
      this(left, null, null);
   }
   
   public Comparison(Evaluation left, StringToken operator, Evaluation right) {
      this.operator = RelationalOperator.resolveOperator(operator);
      this.left = left;
      this.right = right;
   }
   
   @Override
   public Value evaluate(Scope scope, Object context) throws Exception {
      if(right != null) {
         Value leftResult = left.evaluate(scope, null);
         Value rightResult = right.evaluate(scope, null);
         
         return operator.operate(leftResult, rightResult);
      }
      return left.evaluate(scope, null);
   }      
}