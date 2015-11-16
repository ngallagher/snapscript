package org.snapscript.compile.instruction;

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
   
   public Object create(Class type, int size) throws Exception {
      if(type == byte.class || type == Byte.class) {
         return new byte[size];
      }
      if(type == int.class || type == Integer.class) {
         return new int[size];
      }
      if(type == long.class || type == Long.class) {
         return new long[size];
      }
      if(type == double.class || type == Double.class) {
         return new double[size];
      }
      if(type == float.class || type == Float.class) {
         return new float[size];
      }
      if(type == short.class || type == Short.class) {
         return new short[size];
      }
      if(type == char.class || type == Character.class) {
         return new char[size];
      }
      if(type == boolean.class || type == Boolean.class) {
         return new boolean[size];
      }
      if(type == null) {
         return new Object[size];
      }
      return Array.newInstance(type, size);
   }
   
   public Object create(Class type, int first, int second) throws Exception {
      if(type == byte.class || type == Byte.class) {
         return new byte[first][second];
      }
      if(type == int.class || type == Integer.class) {
         return new int[first][second];
      }
      if(type == long.class || type == Long.class) {
         return new long[first][second];
      }
      if(type == double.class || type == Double.class) {
         return new double[first][second];
      }
      if(type == float.class || type == Float.class) {
         return new float[first][second];
      }
      if(type == short.class || type == Short.class) {
         return new short[first][second];
      }
      if(type == char.class || type == Character.class) {
         return new char[first][second];
      }
      if(type == boolean.class || type == Boolean.class) {
         return new boolean[first][second];
      }
      if(type == null) {
         return new Object[first][second];
      }
      return Array.newInstance(type, first, second);
   }
   
   public Object create(Class type, int first, int second, int third) throws Exception {
      if(type == byte.class || type == Byte.class) {
         return new byte[first][second][third];
      }
      if(type == int.class || type == Integer.class) {
         return new int[first][second][third];
      }
      if(type == long.class || type == Long.class) {
         return new long[first][second][third];
      }
      if(type == double.class || type == Double.class) {
         return new double[first][second][third];
      }
      if(type == float.class || type == Float.class) {
         return new float[first][second][third];
      }
      if(type == short.class || type == Short.class) {
         return new short[first][second][third];
      }
      if(type == char.class || type == Character.class) {
         return new char[first][second][third];
      }
      if(type == boolean.class || type == Boolean.class) {
         return new boolean[first][second][third];
      }
      if(type == null) {
         return new Object[first][second][third];
      }
      return Array.newInstance(type, first, second, third);
   }
}
