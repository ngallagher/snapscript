package org.snapscript.core.index;

import java.lang.reflect.Method;

public enum PropertyType {
   GET("get", true),
   SET("set", false),      
   IS("is", true);

   private final String prefix;
   private final boolean read;
   private final int size;

   private PropertyType(String prefix, boolean read) {
      this.size = prefix.length();         
      this.prefix = prefix;
      this.read = read;
   }
   
   public boolean isWrite(Method method) {
      String name = method.getName();
      int length = name.length();
      
      if(name.startsWith(prefix)) {
         Class type = method.getReturnType();
         Class[] types = method.getParameterTypes();
         int count = types.length;

         if(type == void.class) {
            return length > size && count == 1;
         }
      }
      return false;
   }      

   public boolean isRead(Method method) {
      String name = method.getName();
      int length = name.length();
      
      if(name.startsWith(prefix)) {
         Class type = method.getReturnType();
         Class[] types = method.getParameterTypes();
         int count = types.length;

         if(type != void.class) {
            return length > size && count == 0;
         }
      }
      return false;
   }

   public String getProperty(Method method) {
      String name = method.getName();

      if(name.startsWith(prefix)) {
         name = name.substring(size);
      }
      return getPropertyName(name);
   }

   public static String getPropertyName(Method method) {
      PropertyType[] types = PropertyType.values();

      for(PropertyType type : types) {
         if(type.isRead(method)) {
            return type.getProperty(method);
         }
      }
      return method.getName();
   }

   public static String getPropertyName(String name) {
      int length = name.length();

      if(length > 0) {
         char[] array = name.toCharArray();
         char first = array[0];

         if(!isAcronym(array)) {
            array[0] = toLowerCase(first);
         }
         return new String(array);
      }
      return name;
   }

   private static boolean isAcronym(char[] array) {
      if(array.length < 2) {
         return false;
      }
      if(!isUpperCase(array[0])) {
         return false;
      }
      return isUpperCase(array[1]);
   }

   private static char toLowerCase(char value) {
      return Character.toLowerCase(value);
   }

   private static boolean isUpperCase(char value) {
      return Character.isUpperCase(value);
   } 
}
