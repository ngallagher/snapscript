package org.snapscript.parse;

import java.util.Arrays;

public class TextCompressor {
   
   public TextCompressor() {
      super();
   }

   public TextSource compress(String text) {
      if(text != null) {
         char[] source = text.toCharArray();
         int length = source.length;
         
         return compress(source, 0, length);
      }
      return null;
   }
   
   private TextSource compress(char[] source, int offset, int length) {
      short[] lines = new short[length];
      int count = length;
      int read = offset;
      short line = 0;
      int write = 0;

      while(read < count){
         if(startQuote(source, read)){
            int start = write;
            
            while(read + 1 < count) {
               source[write++] = source[read++];
               
               if(finishQuote(source, read, start)) {
                  source[write++] = source[read++];
                  break;
               }               
            }
         } else if(startBlock(source, read)) {
            while(read < count) {
               if(finishBlock(source, read)) {
                  read += 2;
                  break;
               }
               read++;
            }      
         } else if(startComment(source, read)) {
            while(read < count) {
               if(finishComment(source, read)) {
                  line++;
                  read++;
                  break;
               }
               read++;
            }           
         } else if(!space(source, read)) {
            lines[write] = line;
            source[write++] = source[read++];
         } else {
            if(write > 0 && read + 1< count) {
               if(identifier(source, write-1) && identifier(source, read+1)) {
                  lines[write] = line;
                  source[write++] = ' ';               
               }
               if(operator(source, write-1) && operator(source, read+1)) {
                  lines[write] = line;
                  source[write++] = ' ';               
               }               
            }
            read++;
         }
      }
      return create(source, lines, write);
   }
   
   private TextSource create(char[] source, short[] lines, int length) {
      char[] text = Arrays.copyOfRange(source, 0, length);
      short[] reference = Arrays.copyOfRange(lines, 0, length);
      
      return new TextSource(text, reference);
   }
   
   private boolean startBlock(char[] source, int read){
      int count = source.length;
      
      if(read + 1 < count) {
         char current = source[read];
         char next = source[read + 1];
         
         if(current == '/') {
            return next == '*';
         }
      } 
      return false;
   }  
   
   private boolean finishBlock(char[] source, int read){
      int count = source.length;
      
      if(read + 1 < count) {
         char current = source[read];
         char next = source[read + 1];
         
         if(current == '*') {
            return next == '/';
         }
      } 
      return false;
   }    

   private boolean startComment(char[] source, int read){
      int count = source.length;
      
      if(read + 1 < count) {
         char current = source[read];
         char next = source[read + 1];
         
         if(current == '/') {
            return next == '/';
         }
      } 
      return finishComment(source, read);
   }
   
   private boolean finishComment(char[] source, int read) {
      char value = source[read];
      
      if(value == '\n') {
         return true;
      }
      return false;
   }
   
   private boolean startQuote(char[] source, int read) {
      char value = source[read];
      
      if(read > 0) {
         char escape = source[read-1];
         
         if(escape == '\\') {
            return false;
         }
      }
      return value == '\"' || value == '\'';
   }
   
   private boolean finishQuote(char[] source, int read, int start) {
      char open = source[start];
      char value = source[read];
      
      if(read > 0) {
         char previous = source[read-1];

         if(previous == '\\') {
            if(read > 1) {
               char escape = source[read - 2];
               
               if(escape == '\\') {
                  return value == open;
               }
            }
            return false;
         }
      }
      return value == open;
   }   
   
   private boolean space(char[] source, int read) {
      char value = source[read];
      
      switch(value){
      case ' ': case '\t':
      case '\n': case '\r':
         return true;
      default:
         return false;
      }
   }   

   private boolean identifier(char[] source, int read) {
      char value = source[read];
      
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
   
   private boolean operator(char[] source, int read) {
      char value = source[read];
         
      switch(value) {
      case '+': case '-':
      case '/': case '*':
      case '%':
         return true;
      }
      return false;
   } 
}

