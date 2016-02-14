package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class AtomicIntegerConverter extends NumberConverter {

   private static final Class[] ATOMIC_INTEGER_TYPES = {
      AtomicInteger.class, 
      Integer.class,
      Short.class, 
      BigInteger.class, 
      Long.class,
      AtomicLong.class, 
      Double.class, 
      Float.class, 
      BigDecimal.class, 
      Byte.class};
   
   private static final int[] ATOMIC_INTEGER_SCORES = {
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
   
   public AtomicIntegerConverter(Type type) {
      super(type, ATOMIC_INTEGER_TYPES, ATOMIC_INTEGER_SCORES);
   }
}