package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.PrimitivePromoter;

public abstract class ConstraintConverter  {
   
   public static final int EXACT = 100;
   public static final int SIMILAR = 70;
   public static final int COMPATIBLE = 20;
   public static final int POSSIBLE = 10;
   public static final int INVALID = 0;
   
   protected final PrimitivePromoter promoter;
   protected final ConstraintAdapter adapter;
   
   protected ConstraintConverter() {
      this.promoter = new PrimitivePromoter();
      this.adapter = new ConstraintAdapter();
   }
   
   protected Object convert(Class type, String text) throws Exception {
      Class actual = promoter.convert(type);
      
      try {
         if (actual == String.class) {
            return text;
         }
         if (actual == Integer.class) {
            return adapter.createInteger(text);
         }
         if (actual == Double.class) {
            return adapter.createDouble(text);
         }
         if (actual == Float.class) {
            return adapter.createFloat(text);
         }
         if (actual == Boolean.class) {
            return adapter.createBoolean(text);
         }
         if (actual == Byte.class) {
            return adapter.createByte(text);
         }
         if (actual == Short.class) {
            return adapter.createShort(text);
         }
         if (actual == Long.class) {
            return adapter.createLong(text);
         }
         if (actual == AtomicLong.class) {
            return adapter.createAtomicLong(text);
         }
         if (actual == AtomicInteger.class) {
            return adapter.createAtomicInteger(text);
         }
         if (actual == BigDecimal.class) {
            return adapter.createBigDecimal(text);
         }
         if (actual == BigInteger.class) {
            return adapter.createBigInteger(text);
         }
         if (actual == Character.class) {
            return adapter.createCharacter(text);
         }
      } catch(Exception e) {
         throw new IllegalStateException("Could not convert '" + text + "' to " + actual, e);
      }
      throw new IllegalArgumentException("Could not convert '" + text + "' to " + actual);
   }
   
   protected Object convert(Class type, Number number) {
      Class actual = promoter.convert(type);
      
      try {
         if(actual == Number.class) {
            return number;
         }
         if(actual == Double.class) {
            return adapter.createDouble(number);
         }
         if(actual == Float.class) {
            return adapter.createFloat(number);
         }
         if(actual == Integer.class) {
            return adapter.createInteger(number);
         }
         if(actual == Long.class) {
            return adapter.createLong(number);
         }
         if(actual == Byte.class) {
            return adapter.createByte(number);
         }
         if(actual == Short.class) {
            return adapter.createShort(number);
         }
         if(actual == AtomicLong.class) {
            return adapter.createAtomicLong(number);
         }
         if(actual == AtomicInteger.class) {
            return adapter.createAtomicInteger(number);
         }
         if(actual == BigDecimal.class) {
            return adapter.createBigDecimal(number);
         }
         if(actual == BigInteger.class) {
            return adapter.createBigInteger(number);
         }
         if(actual == Character.class) {
            return adapter.createCharacter(number);
         }
      } catch(Exception e) {
         throw new IllegalStateException("Could not convert '" + number + "' to " + type);
      }
      throw new IllegalArgumentException("Could not convert '" + number + "' to " + actual);
   }
   
   public abstract int score(Object type) throws Exception;
   public abstract Object convert(Object value) throws Exception;
}
