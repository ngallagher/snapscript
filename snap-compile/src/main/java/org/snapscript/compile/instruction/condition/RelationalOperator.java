package org.snapscript.compile.instruction.condition;

import org.snapscript.compile.instruction.BooleanValue;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;


public enum RelationalOperator {
   EQUALS("=="){
      @Override
      public Value operate(Value left, Value right) {
         ValueComparator comparator = ValueComparator.resolveComparator(left, right);
         
         if(comparator.compare(left, right) == 0){
            return BooleanValue.TRUE;
         }
         return BooleanValue.FALSE;
      }      
   },  
   NOT_EQUALS("!="){
      @Override
      public Value operate(Value left, Value right) {
         ValueComparator comparator = ValueComparator.resolveComparator(left, right);
         
         if(comparator.compare(left, right) != 0){
            return BooleanValue.TRUE;
         }
         return BooleanValue.FALSE;
      }      
   },  
   GREATER(">"){
      @Override
      public Value operate(Value left, Value right) {
         ValueComparator comparator = ValueComparator.resolveComparator(left, right);
         
         if(comparator.compare(left, right) > 0){
            return BooleanValue.TRUE;
         }
         return BooleanValue.FALSE;
      }      
   },  
   GREATER_OR_EQUALS(">="){
      @Override
      public Value operate(Value left, Value right) {
         ValueComparator comparator = ValueComparator.resolveComparator(left, right);
         
         if(comparator.compare(left, right) >= 0){
            return BooleanValue.TRUE;
         }
         return BooleanValue.FALSE;
      }      
   }, 
   LESS("<"){
      @Override
      public Value operate(Value left, Value right) {
         ValueComparator comparator = ValueComparator.resolveComparator(left, right);
         
         if(comparator.compare(left, right) < 0){
            return BooleanValue.TRUE;
         }
         return BooleanValue.FALSE;
      }      
   }, 
   LESS_OR_EQUALS("<="){
      @Override
      public Value operate(Value left, Value right) {
         ValueComparator comparator = ValueComparator.resolveComparator(left, right);
         
         if(comparator.compare(left, right) <= 0){
            return BooleanValue.TRUE;
         }
         return BooleanValue.FALSE;
      }      
   };
   
   public final String operator;
   
   private RelationalOperator(String operator) {
      this.operator = operator;
   }
   
   public abstract Value operate(Value left, Value right);
   
   public static RelationalOperator resolveOperator(StringToken token) {
      if(token != null) {
         String value = token.getValue();
         RelationalOperator[] operators = RelationalOperator.values();
         
         for(RelationalOperator operator : operators) {
            if(operator.operator.equals(value)) {
               return operator;
            }
         }
      }
      return null;
   }   
}
