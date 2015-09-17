package org.snapscript.core.execute;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ArrayConverter {
   
   public ArrayConverter() {
      super();
   }

   public List convert(Object array) throws Exception {
      Class type = array.getClass();
      
      if(type == byte[].class || type == Byte[].class) {
         return new PrimitiveNumberList(array, Byte.class);
      }
      if(type == int[].class || type == Integer[].class) {
         return new PrimitiveNumberList(array, Integer.class);
      }
      if(type == long[].class || type == Long[].class) {
         return new PrimitiveNumberList(array, Long.class);
      }
      if(type == double[].class || type == Double[].class) {
         return new PrimitiveNumberList(array, Double.class);
      }
      if(type == float[].class || type == Float[].class) {
         return new PrimitiveNumberList(array, Float.class);
      }
      if(type == short[].class || type == Short[].class) {
         return new PrimitiveNumberList(array, Short.class);
      }
      if(type == char[].class || type == Character[].class) {
         return new PrimitiveCharacterList(array, Character.class);
      }
      if(type == boolean[].class || type == Boolean[].class) {
         return new PrimitiveBooleanList(array, Character.class);
      }
      return Arrays.asList((Object[])array);
   }
   
   public Object create(String type, int size) throws Exception {
      if(type.equals("Byte")) {
         return new byte[size];
      }
      if(type.equals("Integer")) {
         return new int[size];
      }
      if(type.equals("Long")) {
         return new long[size];
      }
      if(type.equals("Double")) {
         return new double[size];
      }
      if(type.equals("Float")) {
         return new float[size];
      }
      if(type.equals("Short")) {
         return new short[size];
      }
      if(type.equals("Character")) {
         return new char[size];
      }
      if(type.equals("Boolean")) {
         return new boolean[size];
      }
      return new Object[size];
   }
   
   public Object create(Class type, int size) throws Exception {
      if(type == Byte.class) {
         return new byte[size];
      }
      if(type == Integer.class) {
         return new int[size];
      }
      if(type == Long.class) {
         return new long[size];
      }
      if(type == Double.class) {
         return new double[size];
      }
      if(type == Float.class) {
         return new float[size];
      }
      if(type == Short.class) {
         return new short[size];
      }
      if(type == Character.class) {
         return new char[size];
      }
      if(type == Boolean.class) {
         return new boolean[size];
      }
      return Array.newInstance(type, size);
   }
}
