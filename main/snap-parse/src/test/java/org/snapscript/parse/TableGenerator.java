package org.snapscript.parse;

public class TableGenerator {   
   
   public static final short NONE = 0x0000;
   public static final short LETTER = 0x0001;
   public static final short DIGIT = 0x0002;
   public static final short HEXIDECIMAL = 0x0004;
   public static final short IDENTIFIER = 0x0008;   
   public static final short QUOTE = 0x0010;
   public static final short PERIOD = 0x0020;
   public static final short LONG = 0x0040;
   public static final short DOUBLE = 0x0080;
   public static final short FLOAT = 0x0100;
   public static final short MINUS = 0x0200;
   public static final short UNICODE = 0x0400;    
   public static final short ESCAPE = 0x0800;
   public static final short SPECIAL = 0x1000;
   public static final short DOLLAR = 0x2000;
   public static final short CAPITAL = 0x4000;     
   
   public static void main(String[] list) {
      System.out.println("private static final short[] TYPES = {");
     
      for(int i = 0; i < 0xff; i++) {
         char next = (char)i;
         //System.out.print("/*"+i+"*/");
         if(digit(next)) {
            System.out.println("TextReader.DIGIT | TextReader.HEXIDECIMAL | TextReader.IDENTIFIER,");          
         } else if(letter(next)) {
            System.out.print("TextReader.LETTER | TextReader.IDENTIFIER"); 
            
            if(hexidecimal(next)) {
               System.out.print(" | TextReader.HEXIDECIMAL");
            } 
            if(next == 'l' || next == 'L') {
               System.out.print(" | TextReader.LONG");
            }
            if(next == 'd' || next == 'D') {
               System.out.print(" | TextReader.DOUBLE");
            }
            if(next == 'f' || next == 'F') {
               System.out.print(" | TextReader.FLOAT");
            }
            if(next == 'u' || next == 'U') {
               System.out.print(" | TextReader.UNICODE");
            } 
            if(Character.isUpperCase(next)) {
               System.out.print(" | TextReader.CAPITAL");
            }
            switch(next){
            case '\'': case '"':
            case '\\': case 'n':
            case 't': case 'f':
            case 'u': case 'U':
               System.out.print(" | TextReader.SPECIAL");
            }
            System.out.println(",");
         } else if(qualifier(next)){
            System.out.println("TextReader.PERIOD,"); 
         } else if(quote(next)){
            System.out.print("TextReader.QUOTE");
            switch(next){
            case '\'': case '"':
               System.out.println(" | TextReader.SPECIAL,");
               break;
            default:
               System.err.println(",");
            }            
         } else if(next == '-') {
            System.out.println("TextReader.MINUS,"); 
         } else {
            switch(next){
            case '\'': case '"':
            case '\\': case 'n':
            case 't': case 'f':
            case 'u': case 'U':               
               System.out.print("TextReader.SPECIAL");
               if(next == '\\') {
                  System.out.println(" | TextReader.ESCAPE,");
               } else {
                  System.out.println(",");
               }
               break;
            default:
               if(next=='$'){
                  System.out.println("TextReader.DOLLAR,");
               }else if(next=='_'){
                  System.out.println("TextReader.IDENTIFIER,");
               } else{
                  System.out.println("TextReader.NONE,");
               }
            } 
         } 
      }
     
      System.out.println("};");      
   }
   
   private static boolean hexidecimal(char value) {
      if(value >= 'a' && value <= 'f') {
         return true;
      }
      if(value >= 'A' && value <= 'F') {
         return true;
      }
      return false;
   }
   
   
   
   private static boolean qualifier(char value) {
      return value == '.';
   } 
   
   private static boolean letter(char value) {
      if(value >= 'a' && value <= 'z') {
         return true;
      }
      if(value >= 'A' && value <= 'Z') {
         return true;
      }
      return false;
   }   
   
   private static boolean digit(char value) {
      return value >= '0' && value <= '9';
   }
   
   private static boolean quote(char value) {
      return value == '\'' || value == '"';
   } 
}
