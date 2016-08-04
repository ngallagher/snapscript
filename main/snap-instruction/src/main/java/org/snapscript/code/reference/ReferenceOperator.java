package org.snapscript.code.reference;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.parse.StringToken;

public enum ReferenceOperator {
   NORMAL("."){
      @Override
      public Value operate(Scope scope, Evaluation next, Value value) throws Exception {
         Object object = value.getValue();
         
         if(object != null) {
            return next.evaluate(scope, object);
         }
         throw new NullPointerException("Reference to a null object");
      }      
   }, 
   EXISTENTIAL("?."){
      @Override
      public Value operate(Scope scope, Evaluation next, Value value) throws Exception {
         Object object = value.getValue();
         
         if(object != null) {
            return next.evaluate(scope, object);
         }
         return ValueType.getTransient(object);
      }
   };
   
   private final String symbol;
   
   private ReferenceOperator(String symbol) {
      this.symbol = symbol;
   }
   
   public abstract Value operate(Scope scope, Evaluation next, Value value) throws Exception;
   
   public static ReferenceOperator resolveOperator(StringToken token) {
      if(token != null) {
         String value = token.getValue();
         ReferenceOperator[] operators = ReferenceOperator.values();
         
         for(ReferenceOperator operator : operators) {
            if(operator.symbol.equals(value)) {
               return operator;
            }
         }
      }
      return null;
   }
}
