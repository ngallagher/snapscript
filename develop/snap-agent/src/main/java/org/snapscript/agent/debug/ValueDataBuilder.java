package org.snapscript.agent.debug;

import java.util.Arrays;

import org.snapscript.core.Instance;
import org.snapscript.core.Type;

public class ValueDataBuilder {
   
   private static final int MAX_LENGTH = 1024;

   public static ValueData createNull(String key, Object value, int depth) {
      return new ValueData(key, "", "null", "null", false, depth);
   }
   
   public static ValueData createArray(String key, Object value, int depth) {
      StringBuilder dimensions = new StringBuilder();
      Class type = value.getClass();
      Class entry = type.getComponentType();
      String text = "";
      
      if(type == byte[].class) {
         text = Arrays.toString((byte[])value);
      } else if(type == int[].class) { 
         text = Arrays.toString((int[])value);
      } else if(type == long[].class) {
         text = Arrays.toString((long[])value);
      } else if(type == double[].class) {
         text = Arrays.toString((double[])value);
      } else if(type == float[].class) {
         text = Arrays.toString((float[])value);
      } else if(type == short[].class) {
         text = Arrays.toString((short[])value);
      } else if(type == char[].class) {
         text = Arrays.toString((char[])value);
      } else if(type == boolean[].class) {
         text = Arrays.toString((boolean[])value);
      } else {
         text = Arrays.deepToString((Object[])value); // is this dangerous?
      }
      while(entry != null) {
         dimensions.append("[]");
         type = entry;
         entry = type.getComponentType();
      }
      String name = type.getSimpleName();
      int length = text.length();
      
      if(length > MAX_LENGTH) {
         text = text.substring(0, MAX_LENGTH) + "..."; // truncate value
      }
      return new ValueData(key, name + dimensions, "", text, true, depth);
   }
   
   public static ValueData createObject(String key, Object value, int depth) {
      Class type = value.getClass();
      String name = type.getSimpleName();
      String text = String.valueOf(value);
      int length = text.length();
      
      if(length > MAX_LENGTH) {
         text = text.substring(0, MAX_LENGTH) + "..."; // truncate value
      }
      return new ValueData(key, name, "", text, true, depth);
   }
   
   public static ValueData createPrimitive(String key, Object value, int depth) {
      Class type = value.getClass();
      String name = type.getSimpleName();
      String text = String.valueOf(value);
      int length = text.length();
      
      if(length > MAX_LENGTH) {
         text = text.substring(0, MAX_LENGTH) + "..."; // truncate value
      }
      return new ValueData(key, name, text, text, false, depth);
   }
   
   public static ValueData createScope(String key, Object value, int depth) {
      Instance instance = (Instance)value;
      Type type = instance.getType();
      String name = type.getName();
      return new ValueData(key, name, "", "", true, depth);
   }
}
