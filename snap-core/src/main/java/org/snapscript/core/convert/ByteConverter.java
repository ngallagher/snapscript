package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class ByteConverter extends NumberConverter {

   private static final Class[] BYTE_TYPES = {
      Byte.class, 
      Short.class, 
      Integer.class, 
      AtomicInteger.class, 
      Long.class,
      AtomicLong.class, 
      BigInteger.class, 
      Double.class, 
      Float.class, 
      BigDecimal.class};
   
   private static final int[] BYTE_SCORES = {
      EXACT,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      SIMILAR,
      COMPATIBLE,
      COMPATIBLE,
      COMPATIBLE
   };
   
   public ByteConverter(Type type) {
      super(type, BYTE_TYPES, BYTE_SCORES);
   }
   
   @Override
   public Object convert(Object value) throws Exception {
      Class require = type.getType();
      
      if(value != null) {
         Class actual = value.getClass();
         
         if(actual == String.class) {
            return convert(Byte.class, value);
         }
         Class parent = actual.getSuperclass();
         
         if(parent == Number.class) {
            Number number = (Number)value;
            return number.byteValue();
         }
         throw new IllegalArgumentException("Conversion from " + actual + " to byte is not possible");
      }
      if(require.isPrimitive()) {
         throw new IllegalArgumentException("Invalid conversion from null to primitive byte");
      }
      return null;
   }
}