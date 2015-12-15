package org.snapscript.compile.instruction.operation;

import org.snapscript.core.Reference;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public enum PrefixOperator {
   NOT("!"){
      @Override
      public Value operate(Value right) {
         Boolean value = right.getBoolean();         
         return new Reference(!value);
      }      
   }, 
   COMPLEMENT("~"){
      @Override
      public Value operate(Value right) {      
         Integer value = right.getInteger(); 
         return new Reference(~value);
      }      
   },
   PLUS("+"){
      @Override
      public Value operate(Value right) {
         Integer value = right.getInteger(); 
         return new Reference(+value);
      }      
   },
   MINUS("-"){
      @Override
      public Value operate(Value right) { 
         Integer value = right.getInteger(); 
         return new Reference(-value);
      }      
   };
   
   public final String operator;
   
   private PrefixOperator(String operator){
      this.operator = operator;
   }
   
   public abstract Value operate(Value right);   
   
   public static PrefixOperator resolveOperator(StringToken token) {
      if(token != null) {
         String value = token.getValue();
         PrefixOperator[] operators = PrefixOperator.values();
         
         for(PrefixOperator operator : operators) {
            if(operator.operator.equals(value)) {
               return operator;
            }
         }
      }
      return null;
   }  
}
