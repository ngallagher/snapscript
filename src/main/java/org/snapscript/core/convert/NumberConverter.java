package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NumberConverter extends TypeConverter {
   
   @Override
   public int score(Object value) throws Exception {
      Class type = value.getClass();
      
      if(type == Double.class) {
         return SIMILAR;
      }
      if(type == Float.class) {
         return SIMILAR;
      }
      if(type == BigDecimal.class) {
         return SIMILAR;
      }
      if(type == Long.class) {
         return SIMILAR;
      }
      if(type == AtomicLong.class) {
         return SIMILAR;
      }
      if(type == Integer.class) {
         return SIMILAR;
      }
      if(type == BigInteger.class) {
         return SIMILAR;
      }
      if(type == AtomicInteger.class) {
         return SIMILAR;
      }
      if(type == Short.class) {
         return SIMILAR;
      }
      if(type == Byte.class) {
         return SIMILAR;
      }
      if(type == String.class) {
         return SIMILAR;
      }
      return INVALID;
   }
   
   public Object convert(Object value) throws Exception {
      Class type = value.getClass();
      
      if(type == String.class) {
         String text = String.valueOf(value);
         return convert(Double.class, text);
      }
      Class parent = type.getSuperclass();
      
      if(parent == Number.class) {
         return (Number)value;
      }
      throw new IllegalArgumentException("Conversion from " + type + " to number is not possible");
   }
}