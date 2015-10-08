package org.snapscript.parse;

import junit.framework.TestCase;

public class QuoteParseTest extends TestCase {
   
   public void testParse() throws Exception {
      assertEquals(new Compressor("\"c:\\\\\"".toCharArray()).compress(), "\"c:\\\\\""); // "c:\\"
      assertEquals(new Compressor(new char[]{'"', 'c', ':', '\\', '\\', '\\', '"', '"'}).compress(), new String(new char[]{'"', 'c', ':', '\\', '\\', '\\', '"', '"'})); // "c:\\\""
      assertEquals(new Compressor(new char[]{'"', '"'}).compress(), new String(new char[]{'"', '"'})); // ""
      assertEquals(new Compressor(new char[]{'\'', '\''}).compress(), new String(new char[]{'\'', '\''})); // ''
      assertEquals(new Compressor("/** // */".toCharArray()).compress(), ""); // /** // */
      //assertEquals(new Compressor("/** // *".toCharArray()).compress(), -1); // /** // *
      //assertEquals(new Compressor("/** // *".toCharArray()).compress(), -1); // /** // *
      assertEquals(new Compressor("/* abc */ // another one".toCharArray()).compress(), ""); // /* abc */
      assertEquals(new Compressor("/// this is a comment".toCharArray()).compress(), ""); /// this is a comment
      assertEquals(new Compressor("/// this is a comment\n".toCharArray()).compress(), ""); /// this is a comment
      assertEquals(new Compressor("/// this is a comment\n\n".toCharArray()).compress(), ""); /// this is a comment
   }

   private static class Compressor {
      
      private short[] lines;
      private char[] source;
      private short line;
      private int write;
      private int read;
      private int count;
      
      public Compressor(char[] source) {
         this.lines = new short[source.length];
         this.count = source.length;
         this.source = source;
      }
      
      private String compress() {
         while(read < count) {
            char next = source[read];
            
            if(comment(next)) {
               if(!comment()) {
                  throw new IllegalStateException("Comment not complete");
               }
            } else if(quote(next)) {
               if(!string()){
                  throw new IllegalStateException("Quote not complete");
               }
            } else if(!space(next)) {
               lines[write] = line;
               source[write++] = source[read++];
            } else {
               if(write > 0 && read + 1< count) {
                  char before = source[write-1];
                  char after = source[read-1];
                  
                  if(identifier(before) && identifier(after)) {
                     lines[write] = line;
                     source[write++] = ' ';               
                  }
                  if(operator(before) && operator(after)) {
                     lines[write] = line;
                     source[write++] = ' ';               
                  }               
               }
               read++;
            }
         }
         return new String(source, 0, write);
      }
      
      private boolean comment() {
         char start = source[read];
         
         if(comment(start)){
            if(read + 1 < count) {   
               char next = source[read + 1];
               
               if(next == '/') {
                  while(read < count) {
                     char terminal = source[read];
                     
                     if(terminal == '\n') {
                        line++;
                        return true;
                     }
                     read++;
                  }
                  return true; // end of source
               } 
               if(next == '*') {
                  while(read < count) {
                     char terminal = source[read];
                     
                     if(terminal == '\n') {
                        line++;
                     }
                     if(terminal == '/' && read > 0) {
                        char prev = source[read - 1];
                        
                        if(prev == '*') {
                           read++;
                           return true;
                        }
                     }
                     read++;
                  }
               }
            }
         }
         return false;
      }
      
      private boolean string() {
         char start = source[read];
         
         if(quote(start)) {
            int size = 0;
            
            while(read < count) {
               char next = source[read];
               
               if(next == start) {
                  if(size == 1) { // "" or ''
                     return true; 
                  } 
                  if(read > 0) {
                     char prev = source[read - 1];
                     
                     if(!escape(prev)) {
                        source[write++] = source[read++];
                        return true;
                     }
                     for(int i = 1; i < size; i++) {
                        char value = source[read - i];
                        
                        if(!escape(value)) {
                           if(i % 2 == 1) {
                              source[write++] = source[read++];
                              return true;
                           }
                           break;
                        }
                     }
                  }
               }
               source[write++] = source[read++];
               size++;
            }
         }
         return false;
      }
      
      private boolean escape(char value) {
         return value == '\\';
      }
      
      private boolean quote(char value) {
         return value == '"' || value == '\'';
      }
      
      private boolean comment(char value) {
         return value == '/';
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
