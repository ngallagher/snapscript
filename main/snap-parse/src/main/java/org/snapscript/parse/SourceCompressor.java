package org.snapscript.parse;

import static org.snapscript.parse.TextCategory.INDEX;

public class SourceCompressor {
   
   private static final int LINE_LIMIT = 32000;
   
   private char[] original;
   private char[] compress;
   private short[] lines;
   private short line;
   private int write;
   private int read;
   private int count;
   
   public SourceCompressor(char[] original) {
      this.lines = new short[original.length];
      this.compress = new char[original.length];
      this.count = original.length;
      this.original = original;
      this.line = 1; // lines start at 1
   }
   
   public SourceCode compress() {
      if(read < count) {
         directive(); // read interpreter directive
      }
      while(read < count) {
         char next = original[read];
         
         if(comment(next)) {
            if(!comment()) {
               lines[write] = line;
               compress[write++] = original[read++];
            }
         } else if(quote(next)) {
            if(!string()) {
               lines[write] = line;
               compress[write++] = original[read++];
            }
         } else if(!space(next)) {
            lines[write] = line;
            compress[write++] = original[read++];
         } else {
            if(write > 0 && read + 1< count) {
               char before = compress[write-1];
               char after = original[read+1];
               
               if(identifier(before) && identifier(after)) {
                  lines[write] = line;
                  compress[write++] = ' ';               
               }
               if(operator(before) && operator(after)) {
                  lines[write] = line;
                  compress[write++] = ' ';               
               }               
            }
            if(next == '\n') {
               if(line > LINE_LIMIT) {
                  throw new SourceException("Source exceeds " + LINE_LIMIT + " lines");
               }
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
         throw new SourceException("Source text is empty");
      }
      for(int i = 0; i < write; i++) {
         char next = compress[i];
         
         if(next < INDEX.length) {
            types[i] = INDEX[next];
         }
         index[i] = lines[i]; 
         text[i] = next;
      }
      return new SourceCode(original, text, index, types);
   }
   
   private boolean directive() {
      char start = original[read];
      
      if(directive(start)){
         if(read + 1 < count) {   
            char next = original[read + 1];
            
            if(next == '!') {
               while(read < count) {
                  char terminal = original[read];
                  
                  if(terminal == '\n') {
                     if(line > LINE_LIMIT) {
                        throw new SourceException("Source exceeds " + LINE_LIMIT + " lines");
                     }
                     read++;
                     line++;
                     return true;
                  }
                  read++;
               }
               return true; // end of source
            } 
         }
      }
      return false;
   }
   
   private boolean comment() {
      char start = original[read];
      
      if(comment(start)){
         if(read + 1 < count) {   
            char next = original[read + 1];
            
            if(next == '/') {
               while(read < count) {
                  char terminal = original[read];
                  
                  if(terminal == '\n') {
                     if(line > LINE_LIMIT) {
                        throw new SourceException("Source exceeds " + LINE_LIMIT + " lines");
                     }
                     read++;
                     line++;
                     return true;
                  }
                  read++;
               }
               return true; // end of source
            } 
            if(next == '*') {
               while(read < count) {
                  char terminal = original[read];
                  
                  if(terminal == '\n') {
                     if(line > LINE_LIMIT) {
                        throw new SourceException("Source exceeds " + LINE_LIMIT + " lines");
                     }
                     line++;
                  }
                  if(terminal == '/' && read > 0) {
                     char prev = original[read - 1];
                     
                     if(prev == '*') {
                        read++;
                        return true;
                     }
                  }
                  read++;
               }
               throw new SourceException("Comment not closed at line " + line);
            }
         }
      }
      return false;
   }
   
   private boolean string() {
      char start = original[read];
      
      if(quote(start)) {
         int size = 0;
         
         while(read < count) {
            char next = original[read];
            
            if(next == start) {
               if(size == 1) { // "" or ''
                  lines[write] = line;
                  compress[write++] = original[read++];
                  return true; 
               } 
               if(read > 0 && size > 0) {
                  char prev = original[read - 1];
                  
                  if(!escape(prev)) {
                     lines[write] = line;
                     compress[write++] = original[read++];
                     return true;
                  }
                  for(int i = 1; i <= size; i++) {
                     char value = original[read - i];
                     
                     if(!escape(value)) {
                        if(i % 2 == 1) {
                           lines[write] = line;
                           compress[write++] = original[read++];
                           return true;
                        }
                        break;
                     }
                  }
               }
            }
            if(next == '\n') {
               if(line > LINE_LIMIT) {
                  throw new SourceException("Source exceeds " + LINE_LIMIT + " lines");
               }
               line++;
            }
            lines[write] = line;
            compress[write++] = original[read++];
            size++;
         }
         throw new SourceException("String literal not closed at line " + line);
      }
      return false;
   }
   
   private boolean escape(char value) {
      return value == '\\';
   }
   
   private boolean quote(char value) {
      return value == '"' || value == '\'';
   }
   
   private boolean directive(char value) {
      return value == '#';
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
