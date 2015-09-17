package org.snapscript.core.execute;

public enum ConditionCombiner {
   AND("&&"),
   OR("||");
   
   private final String operator;
  
   private ConditionCombiner(String operator){
      this.operator = operator;
   }
   
  public boolean isAnd() {
     return this == AND;
  }
  
  public boolean isOr() {
     return this == OR;
  }
  
  public static ConditionCombiner resolveCombiner(String token){
     ConditionCombiner[] roperators = ConditionCombiner.values();
     
     for(ConditionCombiner operator : roperators) {
        if(operator.operator.equals(token)) {
           return operator;
        }
     }
     return null;
  }
}
