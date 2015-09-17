package org.snapscript.core.execute;

import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public enum NumericOperator {
   NONE("", 0) {
      @Override
      public Value operate(Value left, Value right) {
         return right;
      }        
   },
   PLUS("+", 1){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Double first = left.getDouble(); 
         Double second = right.getDouble();
         
         return converter.convert(first + second);
      }      
   }, 
   MINUS("-", 1){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Double first = left.getDouble(); 
         Double second = right.getDouble();
         
         return converter.convert(first - second);
      }      
   },
   DIVIDE("/", 2){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Double first = left.getDouble(); 
         Double second = right.getDouble();
         
         return converter.convert(first / second);
      }      
   },
   MULTIPLY("*", 2){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Double first = left.getDouble(); 
         Double second = right.getDouble();
         
         return converter.convert(first * second);
      }      
   }, 
   MODULUS("%", 2){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Double first = left.getDouble(); 
         Double second = right.getDouble();
         
         return converter.convert(first % second);
      }      
   },
   AND("&", 3){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Integer first = left.getInteger();
         Integer second = right.getInteger();
         
         return converter.convert(first & second);
      }      
   },  
   OR("|", 3){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Integer first = left.getInteger();
         Integer second = right.getInteger();
         
         return converter.convert(first | second); 
      }      
   }, 
   XOR("^", 3){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Integer first = left.getInteger();
         Integer second = right.getInteger();
         
         return converter.convert(first ^ second);   
      }      
   },    
   SHIFT_RIGHT(">>", 3){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Integer first = left.getInteger();
         Integer second = right.getInteger();
         
         return converter.convert(first >> second); 
      }      
   }, 
   SHIFT_LEFT("<<", 3){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Integer first = left.getInteger();
         Integer second = right.getInteger();
         
         return converter.convert(first << second);  
      }      
   },  
   UNSIGNED_SHIFT_RIGHT(">>>", 3){
      @Override
      public Value operate(Value left, Value right) {
         NumericConverter converter = NumericConverter.resolveConverter(left, right);
         Integer first = left.getInteger();
         Integer second = right.getInteger();
         
         return converter.convert(first >>> second); 
      }      
   };   
   
   public final String operator;
   public final int priority;
   
   private NumericOperator(String operator, int priority) {
      this.operator = operator;
      this.priority = priority;
   }   
   
   public abstract Value operate(Value left, Value right);
   
   public static NumericOperator resolveOperator(StringToken token) {
      String value = token.getValue();
      NumericOperator[] operators = NumericOperator.values();
      
      for(NumericOperator operator : operators) {
         if(operator.operator.equals(value)) {
            return operator;
         }
      }
      return null;
   }   
}
