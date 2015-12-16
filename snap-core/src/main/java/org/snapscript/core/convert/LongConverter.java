package org.snapscript.core.convert;

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
      Byte.class};
   
   private static final int[] LONG_SCORES = {
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
   
   @Override
   public Object convert(Object value) throws Exception {
      Class require = type.getType();
      
      if(value != null) {
         Class actual = value.getClass();
         
         if(actual == String.class) {
            return convert(Long.class, value);
         }
         Class parent = actual.getSuperclass();
         
         if(parent == Number.class) {
            Number number = (Number)value;
            return number.longValue();
         }
         throw new IllegalArgumentException("Conversion from " + actual + " to long is not possible");
      }
      if(require.isPrimitive()) {
         throw new IllegalArgumentException("Invalid conversion from null to primitive long");
      }
      return null;
   }
}