package org.snapscript.code.operation;

import org.snapscript.core.convert.StringBuilder;
import org.snapscript.core.Evaluation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public class Assignment implements Evaluation {

   private final AssignmentOperator operator;
   private final Evaluation left;
   private final Evaluation right;
   
   public Assignment(Evaluation left, StringToken operator, Evaluation right) {
      this.operator = AssignmentOperator.resolveOperator(operator);
      this.left = left;
      this.right = right;
   }
   
   @Override
   public Value evaluate(Scope scope, Object context) throws Exception { // this is rubbish
      Value leftResult = left.evaluate(scope, context);
      Value rightResult = right.evaluate(scope, context);
      
      if(operator != AssignmentOperator.EQUAL) {
         Object leftValue = leftResult.getValue();
         
         if(!Number.class.isInstance(leftValue)) { 
            Object rightValue = rightResult.getValue();
            
            if(operator != AssignmentOperator.PLUS_EQUAL) {
               throw new InternalStateException("Operator " + operator + " is illegal");         
            }
            String leftText = StringBuilder.create(scope, leftValue);
            String rightText = StringBuilder.create(scope, rightValue);
            String text = leftText.concat(rightText);
            
            leftResult.setValue(text);
            return leftResult;
         }
      }
      return operator.operate(scope, leftResult, rightResult);      
   }
}