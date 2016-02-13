package org.snapscript.compile.instruction.condition;

import org.snapscript.parse.StringToken;

public enum CombinationOperator {
   AND("&&"),
   OR("||");
   
   private final String operator;
   
   private CombinationOperator(String operator){
      this.operator = operator;
   }
   
  public boolean isAnd() {
     return this == AND;
  }
  
  public boolean isOr() {
     return this == OR;
  }
  
  public static CombinationOperator resolveOperator(StringToken token){
     if(token != null) {
        String value = token.getValue();
        CombinationOperator[] operators = CombinationOperator.values();
        
        for(CombinationOperator operator : operators) {
           if(operator.operator.equals(value)) {
              return operator;
           }
        }
     }
     return null;
  }
}