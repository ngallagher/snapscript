package org.snapscript.core.convert;

import static org.snapscript.core.convert.Score.COMPATIBLE;
import static org.snapscript.core.convert.Score.EXACT;
import static org.snapscript.core.convert.Score.SIMILAR;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class LongConverter extends NumberConverter{
   
   private static final Class[] LONG_TYPES = {
      Long.class, 
      Integer.class, 
      BigInteger.class, 
      AtomicInteger.class, 
      AtomicLong.class,
      Double.class, 
      Float.class, 
      BigDecimal.class, 
      Short.class, 
      Byte.class
   };
   
   private static final Score[] LONG_SCORES = {
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
   
   public LongConverter(Type type) {
      super(type, LONG_TYPES, LONG_SCORES);
   }
}