package org.snapscript.parse;

public interface SyntaxReader {
   SyntaxReader mark(int index); 
   boolean literal(String value);   
   boolean decimal();
   boolean hexidecimal();
   boolean identifier();
   boolean qualifier();
   boolean template();
   boolean text();
   boolean type();  
   long position(); 
   void commit();
   int reset();
}