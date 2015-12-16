package org.snapscript.core;

public class PrimitiveParser {

   public Number parseDouble(String text) {
      return Double.parseDouble(text);
   }
   
   public Number parseFloat(String text) {
      return (float)Double.parseDouble(text);
   }
   
   public Number parseInteger(String text) {
      return (int)Double.parseDouble(text);
   }
   
   public Number parseLong(String text) {
      return (long)Double.parseDouble(text);
   }
   
   public Number parseShort(String text) {
      return (short)Double.parseDouble(text);
   }
   
   public Number parseByte(String text) {
      return (byte)Double.parseDouble(text);
   }
   
   public Boolean parseBoolean(String text) {
      return Boolean.parseBoolean(text);
   }
   
   public Character parseCharacter(String text) {
      return text.charAt(0);
   }
}
