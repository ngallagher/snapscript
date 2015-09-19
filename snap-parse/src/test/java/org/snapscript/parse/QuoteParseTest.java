package org.snapscript.parse;

import junit.framework.TestCase;

public class QuoteParseTest extends TestCase {
   
   public void testParse() throws Exception {
      assertEquals(new Compressor("\"c:\\\\\"".toCharArray()).quote(), 6); // "c:\\"
      assertEquals(new Compressor(new char[]{'"', 'c', ':', '\\', '\\', '\\', '"', '"'}).quote(), 8); // "c:\\\""
      assertEquals(new Compressor(new char[]{'"', '"'}).quote(), 2); // ""
      assertEquals(new Compressor(new char[]{'\'', '\''}).quote(), 2); // ''
      assertEquals(new Compressor("/** // */".toCharArray()).comment(), 9); // /** // */
      assertEquals(new Compressor("/** // *".toCharArray()).comment(), -1); // /** // *
      assertEquals(new Compressor("/** // *".toCharArray()).comment(), -1); // /** // *
      assertEquals(new Compressor("/* abc */ // another one".toCharArray()).comment(), 9); // /* abc */
      assertEquals(new Compressor("/// this is a comment".toCharArray()).comment(), 21); /// this is a comment
      assertEquals(new Compressor("/// this is a comment\n".toCharArray()).comment(), 22); /// this is a comment
      assertEquals(new Compressor("/// this is a comment\n\n".toCharArray()).comment(), 22); /// this is a comment
   }
   private static enum Error {
      QUOTE("Quote not terminated"),
      COMMENT("Comment not terminated");
      
      private final String message;
      
      private Error(String message) {
         this.message = message;
      }
   }
   private static class Compressor {
      
      private char[] source;
      private int off;
      private int count;
      
      public Compressor(char[] source) {
         this.count = source.length;
         this.source = source;
      }
      
      public int comment() {
         char start = source[off];
         int pos = off + 1;
         
         if(start == '/' && pos + 1 < count) {
            char next = source[pos];
            
            if(next == '/') {
               int size = 1;
               
               while(pos < count) {
                  char terminal = source[pos];
                  
                  if(terminal == '\n') {
                     return size + 1;
                  }
                  size++;
                  pos++;
               }
               return size; // end of source
            } 
            if(next == '*') {
              int size = 1;
               
               while(pos < count) {
                  char terminal = source[pos];
                  
                  if(terminal == '/') {
                     char prev = source[pos - 1];
                     
                     if(prev == '*') {
                        return size + 1;
                     }
                  }
                  size++;
                  pos++;
               }
            }
            return -1; // error
         }
         return 0;
      }
      
      public int quote() {
         char start = source[off];
         int pos = off + 1;
         
         if(start == '"' || start == '\'') {
            int size = 1;
            
            while(pos < count) {
               char next = source[pos];
               
               if(next == start) {
                  if(size == 1) { // "" or ''
                     return size + 1; 
                  } 
                  char prev = source[pos - 1];
                  
                  if(prev != '\\') {
                     return size + 1;
                  }
                  for(int i = 1; i < size; i++) {
                     char escape = source[pos - i];
                     
                     if(escape != '\\') {
                        if(i % 2 == 1) { 
                           return size + 1;
                        }
                        break;
                     }
                  }
               }
               size++;
               pos++;
            }
            return -1; // error
         }
         return 0;
      }
      
      private boolean space(char value) {
         switch(value){
         case ' ': case '\t':
         case '\n': case '\r':
            return true;
         default:
            return false;
         }
      }   

      private boolean identifier(char value) {
         if(value >= 'a' && value <= 'z') {
            return true;
         }
         if(value >= 'A' && value <= 'Z') {
            return true;
         }
         if(value >= '0' && value <= '9') {
            return true;
         }
         return false;
      }
      
      private boolean operator(char value) {
         switch(value) {
         case '+': case '-':
         case '/': case '*':
         case '%':
            return true;
         }
         return false;
      }
   }
}
