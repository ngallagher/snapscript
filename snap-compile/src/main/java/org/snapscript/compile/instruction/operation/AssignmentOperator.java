package org.snapscript.compile.instruction.operation;

import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public enum AssignmentOperator {
   EQUAL(NumericOperator.NONE, "="), 
   PLUS_EQUAL(NumericOperator.PLUS, "+="), 
   MINUS_EQUAL(NumericOperator.MINUS, "-="), 
   MLTIPLY_EQUAL(NumericOperator.MULTIPLY, "*="), 
   MODULUS_EQUAL(NumericOperator.MODULUS, "%="), 
   DIVIDE_EQUAL(NumericOperator.DIVIDE,"/="),  
   AND_EQUAL(NumericOperator.AND, "&="),    
   OR_EQUAL(NumericOperator.OR, "|="),
   XOR_EQUAL(NumericOperator.XOR, "^="),    
   SHIFT_RIGHT_EQUAL(NumericOperator.SHIFT_RIGHT, ">>="),
   SHIFT_LEFT_EQUAL(NumericOperator.SHIFT_LEFT, "<<="),
   UNSIGNED_SHIFT_RIGHT_EQUAL(NumericOperator.UNSIGNED_SHIFT_RIGHT, ">>>=");
   
   private final NumericOperator operator;
   private final String symbol;
   
   private AssignmentOperator(NumericOperator operator, String symbol) {
      this.operator = operator;
      this.symbol = symbol;
   }
   
   public Value operate(Value left, Value right) {
      Value result = operator.operate(left, right);
      Object value = result.getValue();
      
      left.setValue(value);
      return left;
   }
   
   public static AssignmentOperator resolveOperator(StringToken token) {
      String value = token.getValue();
      AssignmentOperator[] roperators = AssignmentOperator.values();
      
      for(AssignmentOperator operator : roperators) {
         if(operator.symbol.equals(value)) {
            return operator;
         }
      }
      return null;
   }
}
