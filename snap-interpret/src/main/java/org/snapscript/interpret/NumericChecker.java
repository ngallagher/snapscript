package org.snapscript.interpret;

public class NumericChecker {

   public static boolean bothNumeric(Object left, Object right){
      if(!Number.class.isInstance(left)) {
         return false;         
      }
      if(!Number.class.isInstance(right)) {
         return false;
      }
      return true;
   }
}
