package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class IntegerConverter extends NumberConverter {
   
   private static final Class[] INTEGER_TYPES = {
      Integer.class, 
      Long.class, 
      BigInteger.class, 
      AtomicInteger.class, 
      AtomicLong.class,
      Double.class, 
      Float.class, 
      BigDecimal.class, 
      Short.class, 
      Byte.class
   };
   
   private static final int[] INTEGER_SCORES = {
      EXACT,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE
   };

   public IntegerConverter(Type type) {
      super(type, INTEGER_TYPES, INTEGER_SCORES);
   }
}