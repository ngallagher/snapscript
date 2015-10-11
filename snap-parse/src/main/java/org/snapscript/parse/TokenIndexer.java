package org.snapscript.parse;

import static org.snapscript.parse.TokenType.DECIMAL;
import static org.snapscript.parse.TokenType.HEXIDECIMAL;
import static org.snapscript.parse.TokenType.IDENTIFIER;
import static org.snapscript.parse.TokenType.INTEGER;
import static org.snapscript.parse.TokenType.LITERAL;
import static org.snapscript.parse.TokenType.QUALIFIER;
import static org.snapscript.parse.TokenType.TEXT;
import static org.snapscript.parse.TokenType.TYPE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TokenIndexer {

   private final LengthComparator comparator;
   private final GrammarIndexer indexer;
   private final TextReader reader;
   private final List<String> values;
   private final short[] lines;

   public TokenIndexer(GrammarIndexer indexer, char[] source, short[] lines, short[] types, int off, int count) {
      this.reader = new TextReader(source, types, off, count);
      this.comparator = new LengthComparator();
      this.values = new ArrayList<String>();
      this.indexer = indexer;
      this.lines = lines;
   }

   public int[] index(List<Token> tokens) {
      if(values.isEmpty()) {
         List<String> literals = indexer.list();
        
         for(String literal : literals) {
            values.add(literal);
         }
         Collections.sort(values, comparator);
      }
      return scan(tokens);
   }
   
   private int[] scan(List<Token> tokens) {
      int count = reader.count();

      while (true) {
         int mark = reader.mark();
         
         if(mark >= count) {
            return create(tokens);
         }
         int line = lines[mark];
         Token token = literal(line);
         
         if (token == null) {
            token = text(line);
         }
         if(token == null) {
            token = type(line);
         }
         if(token == null) {
            token = identifier(line);
         }
         if(token == null) {
            token = hexidecimal(line);
         }
         if(token == null) {
            token = decimal(line);
         }
         if(token == null) {
            token = integer(line);
         }
         if(token == null) {
            throw new IllegalStateException("Could not parse token on line " + lines[mark]);
         } 
         tokens.add(token);
      }
   }
   
   private int[] create(List<Token> tokens) {
      int length = tokens.size();
      
      if(length > 0) {
         int[] masks = new int[length];
         
         for(int i = 0; i < length; i++) {
            Token token = tokens.get(i);
            
            if(token != null) {
               masks[i] = token.getType();
            }
         }
         return masks;
      }
      return new int[]{};
   }
   
   private Token type(int line) {
      String token = reader.type();

      if (token != null) {
         return new StringToken(token, line, TYPE.mask | QUALIFIER.mask | IDENTIFIER.mask);
      }
      return null;
   }

   private Token identifier(int line) {
      String token = reader.identifier();
      
      if (token != null) {
         return new StringToken(token, line, IDENTIFIER.mask | QUALIFIER.mask);
      }
      return null;
   }

   private Token decimal(int line) {
      Number token = reader.decimal();

      if (token != null) {
         int mask = DECIMAL.mask;

         if (token instanceof Integer || token instanceof Long) {
            mask |= INTEGER.mask;
         }
         return new NumberToken(token, line, mask);
      }
      return null;
   }

   private Token integer(int line) {
      Number token = reader.integer();
      
      if (token != null) {
         return new NumberToken(token, line, INTEGER.mask | DECIMAL.mask);
      }
      return null;
   }

   private Token hexidecimal(int line) {
      Number token = reader.hexidecimal();
      
      if (token != null) {
         return new NumberToken(token, line, INTEGER.mask | HEXIDECIMAL.mask | DECIMAL.mask);
      }
      return null;
   }

   private Token text(int line) {
      String token = reader.text();
      
      if (token != null) {
         return new StringToken(token, line, TEXT.mask);
      }
      return null;
   }
   
   private Token literal(int line) {
      for (String literal : values) {
         int mark = reader.mark();
         String token = reader.literal(literal);

         if (token != null) {
            int length = token.length();
            Character next = token.charAt(length - 1);
            Character peek = reader.peek();
            
            if (identifier(next) && identifier(peek)) {
               reader.reset(mark);
            } else {
               return new StringToken(token, line, LITERAL.mask);
            }
         }
      }
      return null;
   }

   private boolean identifier(Character value) {
      if (value != null) {
         if (Character.isLetter(value)) {
            return true;
         }
         if (Character.isDigit(value)) {
            return true;
         }
      }
      return false;
   }
}
