package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class NumberConverter extends ConstraintConverter {
   
   private static final Class[] NUMBER_TYPES = {
      Integer.class, 
      Long.class, 
      Double.class, 
      Float.class, 
      Short.class, 
      Byte.class,
      BigInteger.class, 
      AtomicInteger.class, 
      AtomicLong.class,
      BigDecimal.class
   };
   
   private static final int[] NUMBER_SCORES = {
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR
   };
   
   protected final NumberMatcher matcher;
   protected final ScoreChecker checker;
   protected final Type type;
   
   public NumberConverter(Type type) {
      this(type, NUMBER_TYPES, NUMBER_SCORES);
   }
   
   public NumberConverter(Type type, Class[] types, int[] scores) {
      this.checker = new ScoreChecker(types, scores);
      this.matcher = new NumberMatcher();
      this.type = type;
   }
   
   @Override
   public int score(Object value) throws Exception {
      Class require = type.getType();
      
      if(value != null) {
         Class actual = value.getClass();
         Integer score = checker.score(actual);
         
         if(score == null) {
            if(actual == String.class) {
               String text = String.valueOf(value);
               NumberType type = matcher.matchNumber(text);
               
               if(type.isDecimal()) {
                  return POSSIBLE;
               }
            }
            return INVALID;
         }
         return score;
      }
      if(require.isPrimitive()) {
         return INVALID;
      }
      return POSSIBLE;
   }
   
   @Override
   public Object convert(Object value) throws Exception {
      Class require = type.getType();
      
      if(value != null) {
         Class actual = value.getClass();
         
         if(actual == String.class) {
            return convert(require, (String)value);
         }
         Class parent = actual.getSuperclass();
         
         if(parent == Number.class) {
            return convert(require, (Number)value);
         }
         throw new IllegalArgumentException("Conversion from " + actual + " to " + require + " is not possible");
      }
      if(require.isPrimitive()) {
         throw new IllegalArgumentException("Invalid conversion from null to primitive number");
      }
      return null;
   }
}