package org.snapscript.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DoubleConverter extends TypeConverter {
   
   @Override
   public int score(Object value) throws Exception {
      Class type = value.getClass();
      
      if(type == Double.class) {
         return EXACT;
      }
      if(type == Float.class) {
         return SIMILAR;
      }
      if(type == BigDecimal.class) {
         return SIMILAR;
      }
      if(type == Long.class) {
         return COMPATIBLE;
      }
      if(type == AtomicLong.class) {
         return COMPATIBLE;
      }
      if(type == Integer.class) {
         return COMPATIBLE;
      }
      if(type == BigInteger.class) {
         return COMPATIBLE;
      }
      if(type == AtomicInteger.class) {
         return COMPATIBLE;
      }
      if(type == Short.class) {
         return COMPATIBLE;
      }
      if(type == Byte.class) {
         return COMPATIBLE;
      }
      if(type == String.class) {
         return POSSIBLE;
      }
      return INVALID;
   }
   
   @Override
   public Object convert(Object value) throws Exception {
      Class type = value.getClass();
      
      if(type == String.class) {
         String text = String.valueOf(value);
         return convert(Double.class, text);
      }
      Class parent = type.getSuperclass();
      
      if(parent == Number.class) {
         Number number = (Number)value;
         return number.doubleValue();
      }
      throw new IllegalArgumentException("Conversion from " + type + " to double is not possible");
   }
}