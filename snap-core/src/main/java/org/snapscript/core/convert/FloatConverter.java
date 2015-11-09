package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Type;

public class FloatConverter extends TypeConverter {
   
   private final Type type;
   
   public FloatConverter(Type type) {
      this.type = type;
   }
   
   @Override
   public int score(Object value) throws Exception {
      Class actual = type.getType();
      
      if(value != null) {
         return match(value);
      }
      if(actual.isPrimitive()) {
         return INVALID;
      }
      return POSSIBLE;
   }
   
   private int match(Object value) throws Exception {
      Class type = value.getClass();
      
      if(type == Float.class) {
         return EXACT;
      }
      if(type == Double.class) {
         return SIMILAR;
      }
      if(type == BigDecimal.class) {
         return SIMILAR;
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
      if(type == Long.class) {
         return COMPATIBLE;
      }
      if(type == AtomicLong.class) {
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
         return convert(Float.class, text);
      }
      Class parent = type.getSuperclass();
      
      if(parent == Number.class) {
         Number number = (Number)value;
         return number.floatValue();
      }
      throw new IllegalArgumentException("Conversion from " + type + " to float is not possible");
   }
}