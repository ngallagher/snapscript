package org.snapscript.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IntegerConverter extends TypeConverter {
   
   @Override
   public int score(Object value) throws Exception {
      Class type = value.getClass();
      
      if(type == Integer.class) {
         return EXACT;
      }
      if(type == Long.class) {
         return SIMILAR;
      }
      if(type == BigInteger.class) {
         return SIMILAR;
      }
      if(type == AtomicInteger.class) {
         return SIMILAR;
      }
      if(type == AtomicLong.class) {
         return SIMILAR;
      }
      if(type == Double.class) {
         return COMPATIBLE;
      }
      if(type == Float.class) {
         return COMPATIBLE;
      }
      if(type == BigDecimal.class) {
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
         return convert(Integer.class, text);
      }
      Class parent = type.getSuperclass();
      
      if(parent == Number.class) {
         Number number = (Number)value;
         return number.intValue();
      }
      throw new IllegalArgumentException("Conversion from " + type + " to integer is not possible");
   }
}