package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class ShortConverter extends NumberConverter {

   private static final Class[] SHORT_TYPES = {
      Short.class, 
      Integer.class, 
      BigInteger.class, 
      AtomicInteger.class, 
      Long.class,
      AtomicLong.class, 
      Double.class, 
      Float.class, 
      BigDecimal.class, 
      Byte.class
   };
   
   private static final int[] SHORT_SCORES = {
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
   
   public ShortConverter(Type type) {
      super(type, SHORT_TYPES, SHORT_SCORES);
   }
}