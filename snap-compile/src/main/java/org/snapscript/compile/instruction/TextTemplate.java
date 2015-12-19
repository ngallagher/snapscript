package org.snapscript.compile.instruction;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.parse.StringToken;

public class TextTemplate implements Evaluation {
   
   private static final short[] IDENTIFIER = {
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
   0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 
   0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
   1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 
   1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
   1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };

   private volatile StringToken template;
   private volatile List<Token> tokens;
   
   public TextTemplate(StringToken template) {
      this.tokens = new ArrayList<Token>();
      this.template = template;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      String text = template.getValue();
      
      if(text == null) {
         throw new IllegalStateException("Text value was null");
      }
      String result = interpolate(scope, text);
   
      return ValueType.getTransient(result);
   }
   
   private String interpolate(Scope scope, String text) throws Exception {
      StringWriter writer = new StringWriter();
            
      if(tokens.isEmpty()) {
         TokenIterator iterator = new TokenIterator(text);
         List<Token> list = new ArrayList<Token>();
         
         while(iterator.hasNext()) {
            Token token = iterator.next();
            
            if(token != null) {
               list.add(token);  
            }
         }
         tokens = list; // atomic swap
      }
      for(Token token : tokens) {
         token.process(scope, writer);
      }
      return writer.toString();
   }
   
   private class TokenIterator {
      
      private char[] source;
      private int off;
      
      public TokenIterator(String text) {
         this.source = text.toCharArray();
      }
      
      public Token next() {
         int mark = off;
         
         while(off < source.length){
            char next = source[off];

            if(next == '$') {
               if(off > mark) {
                  return new TextToken(source, mark, off - mark);
               }
            } else if(off > 0) {
               char prev = source[off - 1];
               
               if(next == '{' && prev == '$') {
                  int start = off + 1;
                  
                  while(off < source.length) {
                     char symbol = source[off++];
                     
                     if(symbol == '}') {
                        return new VariableToken(source, mark, off - mark);
                     } 
                     if(off > start) {
                        if(IDENTIFIER.length < symbol) {
                           return new TextToken(source, mark, off - mark);
                        }
                        if(IDENTIFIER[symbol] == 0) {
                           return new TextToken(source, mark, off - mark);
                        }
                     }
                  }
                  return new TextToken(source, mark, (off - 1) - mark);
               }
            }
            off++;
         }
         if(off > mark) {
            return new TextToken(source, mark, off - mark);
         }
         return null;
      } 
      
      public boolean hasNext() {
         return off < source.length;
      }
   }
   
   private interface Token {
      void process(Scope scope, Writer writer) throws Exception; 
   }
   
   private class TextToken implements Token {
      
      private char[] source;
      private int off;
      private int length;
      
      public TextToken(char[] source, int off, int length) {
         this.source = source;
         this.length = length;
         this.off = off;         
      }
      
      @Override
      public void process(Scope scope, Writer writer) throws Exception {
         writer.write(source, off, length);
      } 
      
      @Override
      public String toString() {
         return new String(source, off, length);
      }
   }
      
   private class VariableToken implements Token {
      
      private String variable;
      private char[] source;
      private int off;
      private int length;
      
      public VariableToken(char[] source, int off, int length) {
         this.variable = new String(source, off + 2, length - 3);
         this.source = source;
         this.length = length;
         this.off = off;         
      }
      
      @Override
      public void process(Scope scope, Writer writer) throws Exception {
         State state = scope.getState();
         Value value = state.getValue(variable);
         
         if(value == null) {
            writer.write(source, off, length);
         } else {
            Object token = value.getValue();
            String text = String.valueOf(token);
            
            writer.append(text);            
         }
      }   
      
      @Override
      public String toString() {
         return new String(source, off, length);
      }
   }
}


