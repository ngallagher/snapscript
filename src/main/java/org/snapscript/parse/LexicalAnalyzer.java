package org.snapscript.parse;

public interface LexicalAnalyzer {      
   Token<String> text();
   Token<String> type();   
   Token<String> identifier();
   Token<String> qualifier();
   Token<String> literal(String text);
   Token<Number> hexidecimal();
   Token<Number> decimal();
   Token<Number> integer();
   int reset(int mark);
   int line(int mark);
   int count();
   int mark();  
}
