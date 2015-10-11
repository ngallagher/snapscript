package org.snapscript.parse;

import static org.snapscript.parse.TextCategory.INDEX;

public class SourceCompressor {
   
   private short[] lines;
   private char[] source;
   private short line;
   private int write;
   private int read;
   private int count;
   
   public SourceCompressor(char[] source) {
      this.lines = new short[source.length];
      this.count = source.length;
      this.source = source;
      this.line = 1; // lines start at 1
   }
   
   public SourceCode compress() {
      while(read < count) {
         char next = source[read];
         
         if(comment(next)) {
            if(!comment()) {
               lines[write] = line;
               source[write++] = source[read++];
            }
         } else if(quote(next)) {
            if(!string()) {
               lines[write] = line;
               source[write++] = source[read++];
            }
         } else if(!space(next)) {
            lines[write] = line;
            source[write++] = source[read++];
         } else {
            if(write > 0 && read + 1< count) {
               char before = source[write-1];
               char after = source[read+1];
               
               if(identifier(before) && identifier(after)) {
                  lines[write] = line;
                  source[write++] = ' ';               
               }
               if(operator(before) && operator(after)) {
                  lines[write] = line;
                  source[write++] = ' ';               
               }               
            }
            if(next == '\n') {
               line++;
            }
            read++;
         }
      }
      return create();
   }
   
   private SourceCode create() {
      char[] text = new char[write];
      short[] index = new short[write]; 
      short[] types = new short[write];
      
      if(write == 0) {
         throw new IllegalStateException("Source text is empty");
      }
      for(int i = 0; i < write; i++) {
         char next = source[i];
         
         if(next < INDEX.length) {
            types[i] = INDEX[next];
         }
         index[i] = lines[i]; 
         text[i] = next;
      }
      return new SourceCode(text, index, types);
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
               throw new IllegalStateException("Comment not closed at line " + line);
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
                  source[write++] = source[read++];
                  return true; 
               } 
               if(read > 0 && size > 0) {
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
         throw new IllegalStateException("String literal not closed at line " + line);
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
