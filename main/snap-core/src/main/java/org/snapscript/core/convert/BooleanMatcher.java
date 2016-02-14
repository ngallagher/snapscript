package org.snapscript.core.convert;

public class BooleanMatcher {

   private static final String TRUE = "true";
   private static final String FALSE = "false";
   
   public boolean matchBoolean(String text) {
      if(text != null) {
         if(text.equalsIgnoreCase(TRUE)) {
            return true;
         }
         if(text.equalsIgnoreCase(FALSE)) {
            return true;
         }
      }
      return false;
   }
}
