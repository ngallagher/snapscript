package org.snapscript.agent.debug;

import org.snapscript.core.InstanceScope;
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
      
      while(entry != null) {
         dimensions.append("[]");
         type = entry;
         entry = type.getComponentType();
      }
      String name = type.getSimpleName();
      return new ValueData(key, name, "", "", true, depth);
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
      InstanceScope instance = (InstanceScope)value;
      Type type = instance.getType();
      String name = type.getName();
      return new ValueData(key, name, "", "", true, depth);
   }
}
