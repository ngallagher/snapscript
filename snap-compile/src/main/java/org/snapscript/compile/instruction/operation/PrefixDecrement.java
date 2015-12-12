package org.snapscript.compile.instruction.operation;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.PrimitivePromoter;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.Token;

public class PrefixDecrement implements Evaluation {
   
   private final PrimitivePromoter promoter;
   private final Evaluation evaluation;
   private final Token operator;
   
   public PrefixDecrement(Token operator, Evaluation evaluation) {
      this.promoter = new PrimitivePromoter();
      this.evaluation = evaluation;
      this.operator = operator;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception { // this is rubbish
      Value reference = evaluation.evaluate(scope, left);
      Number value = reference.getNumber();
      Class type = value.getClass();
      Class actual = promoter.convert(type);
      
      if(actual == Double.class) {
         double result = value.doubleValue();
         reference.setValue(result - 1.0d);
      } else if(actual == Float.class) {
         float result = value.floatValue();
         reference.setValue(result - 1.0f);
      } else if(actual == Long.class) {
         long result = value.longValue();
         reference.setValue(result - 1L);
      } else if(actual == Integer.class) {
         int result = value.intValue();
         reference.setValue(result - 1);
      } else if(actual == Short.class) {
         short result = value.shortValue();
         reference.setValue(result - 1);
      } else if(actual == Byte.class) {
         byte result = value.byteValue();
         reference.setValue(result - 1);
      }
      return reference;
   }
}