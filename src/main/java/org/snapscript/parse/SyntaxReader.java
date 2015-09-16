package org.snapscript.parse;

public interface SyntaxReader {
   SyntaxReader mark(int index); 
   boolean literal(String value);   
   boolean decimal();
   boolean integer();
   boolean hexidecimal();
   boolean identifier();
   boolean qualifier();
   boolean type();  
   boolean text();
   long position(); 
   void commit();
   int reset();
}