package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class BigIntegerConverter extends NumberConverter {

   private static final Class[] BIG_INTEGER_TYPES = {
      BigInteger.class, 
      Integer.class, 
      AtomicInteger.class, 
      Short.class, 
      Long.class,
      AtomicLong.class, 
      Double.class, 
      Float.class, 
      BigDecimal.class, 
      Byte.class
   };
   
   private static final int[] BIG_INTEGER_SCORES = {
      EXACT,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE
   };
   
   public BigIntegerConverter(Type type) {
      super(type, BIG_INTEGER_TYPES, BIG_INTEGER_SCORES);
   }
}
