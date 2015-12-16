package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class DoubleConverter extends NumberConverter {
   
   private static final Class[] DOUBLE_TYPES = {
      Double.class, 
      Float.class, 
      BigDecimal.class, 
      Long.class, 
      AtomicLong.class,
      Integer.class, 
      BigInteger.class, 
      AtomicInteger.class, 
      Short.class, 
      Byte.class};
   
   private static final int[] DOUBLE_SCORES = {
      EXACT,
      SIMILAR,
      SIMILAR,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE
   };
   
   public DoubleConverter(Type type) {
      super(type, DOUBLE_TYPES, DOUBLE_SCORES);
   }

   @Override
   public Object convert(Object value) throws Exception {
      Class require = type.getType();
      
      if(value != null) {
         Class actual = value.getClass();
         
         if(actual == String.class) {
            return convert(Double.class, value);
         }
         Class parent = actual.getSuperclass();
         
         if(parent == Number.class) {
            Number number = (Number)value;
            return number.doubleValue();
         }
         throw new IllegalArgumentException("Conversion from " + actual + " to double is not possible");
      }
      if(require.isPrimitive()) {
         throw new IllegalArgumentException("Invalid conversion from null to primitive double");
      }
      return null;
   }
}