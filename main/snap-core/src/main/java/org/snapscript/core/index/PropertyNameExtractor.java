package org.snapscript.core.index;

import java.lang.reflect.Method;

import org.snapscript.core.function.Function;

public class PropertyNameExtractor {
   
   private static final String[] PREFIXES = {"get", "set", "is"}; 
   
   public static String getProperty(Method method) {
      String name = method.getName();
      
      for(String prefix : PREFIXES) {
         String property = getProperty(name, prefix);
         
         if(property != null) {
            return property;
         }
      }
      return name;
   }
   
   public static String getProperty(Function function) {
      String name = function.getName();
      
      for(String prefix : PREFIXES) {
         String property = getProperty(name, prefix);
         
         if(property != null) {
            return property;
         }
      }
      return name;
   }
   
   public static String getProperty(String function, String prefix) {
      int actual = function.length();
      int require = prefix.length();
      
      if(actual > require) {
         if(function.startsWith(prefix)) {
            String token = function.substring(require);
            return getProperty(token);
         }
      }
      return function;
   }

   private static String getProperty(String name) {
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
