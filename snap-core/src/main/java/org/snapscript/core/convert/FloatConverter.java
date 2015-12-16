package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class FloatConverter extends NumberConverter {
   
   private static final Class[] FLOAT_TYPES = {
      Float.class, 
      Double.class, 
      BigDecimal.class, 
      Long.class, 
      AtomicLong.class,
      Integer.class, 
      BigInteger.class, 
      AtomicInteger.class, 
      Short.class, 
      Byte.class};
   
   private static final int[] FLOAT_SCORES = {
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
   
   public FloatConverter(Type type) {
      super(type, FLOAT_TYPES, FLOAT_SCORES);
   }

   @Override
   public Object convert(Object value) throws Exception {
      Class require = type.getType();
      
      if(value != null) {
         Class actual = value.getClass();
         
         if(actual == String.class) {
            return convert(Float.class, value);
         }
         Class parent = actual.getSuperclass();
         
         if(parent == Number.class) {
            Number number = (Number)value;
            return number.floatValue();
         }
         throw new IllegalArgumentException("Conversion from " + actual + " to float is not possible");
      }
      if(require.isPrimitive()) {
         throw new IllegalArgumentException("Invalid conversion from null to primitive float");
      }
      return null;
   }
}