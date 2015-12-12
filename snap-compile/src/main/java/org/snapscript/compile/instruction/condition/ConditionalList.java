package org.snapscript.compile.instruction.condition;

import org.snapscript.compile.instruction.BooleanValue;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class ConditionalList implements Evaluation {
   
   private final ConditionalPart[] parts;
   
   public ConditionalList(ConditionalPart... parts) {
      this.parts = parts;
   }
   
   @Override
   public Value evaluate(Scope scope, Object context) throws Exception { // this is rubbish
      Boolean result = evaluate(scope, context, 0);
      
      for(int i = 1; i < parts.length; i+=2) {
         CombinationOperator operator = parts[i].getOperator();
         
         if(operator.isAnd()) {
            if(result.booleanValue()) {                  
               result = evaluate(scope, context, i + 1);
            } else {
               return BooleanValue.FALSE;
            }
         } else if(operator.isOr()){
            if(!result.booleanValue()) {
               result = evaluate(scope, context, i + 1);
            } else {
               return BooleanValue.TRUE;
            }
         }
      }
      if(result.booleanValue()) { 
         return BooleanValue.TRUE;
      }
      return BooleanValue.FALSE;
   } 
   
   private Boolean evaluate(Scope scope, Object context, int off) throws Exception {
      ConditionalPart part = parts[off];
      Evaluation evaluation = part.getEvaluation();         
      Value reference = evaluation.evaluate(scope, context);
      try{
      return reference.getBoolean();
      }catch(Exception e){
         e.printStackTrace();
      }
      return null;
   }
}