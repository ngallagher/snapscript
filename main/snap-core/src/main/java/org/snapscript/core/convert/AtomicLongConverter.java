package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class AtomicLongConverter extends NumberConverter {

   private static final Class[] ATOMIC_LONG_TYPES = {
      AtomicLong.class,
      Long.class, 
      Integer.class, 
      BigInteger.class, 
      AtomicInteger.class, 
      Double.class, 
      Float.class, 
      BigDecimal.class, 
      Short.class, 
      Byte.class};
   
   private static final int[] ATOMIC_LONG_SCORES = {
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
   
   public AtomicLongConverter(Type type) {
      super(type, ATOMIC_LONG_TYPES, ATOMIC_LONG_SCORES);
   }
}
