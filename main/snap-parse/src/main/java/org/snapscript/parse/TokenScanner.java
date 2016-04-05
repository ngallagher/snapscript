package org.snapscript.parse;

import static org.snapscript.parse.TokenType.BINARY;
import static org.snapscript.parse.TokenType.DECIMAL;
import static org.snapscript.parse.TokenType.HEXIDECIMAL;
import static org.snapscript.parse.TokenType.IDENTIFIER;
import static org.snapscript.parse.TokenType.LITERAL;
import static org.snapscript.parse.TokenType.QUALIFIER;
import static org.snapscript.parse.TokenType.TEMPLATE;
import static org.snapscript.parse.TokenType.TEXT;
import static org.snapscript.parse.TokenType.TYPE;

import java.util.ArrayList;
import java.util.List;

public class TokenScanner implements LexicalAnalyzer {

   private TokenIndexer indexer;
   private List<Token> tokens;
   private int[] masks;
   private int count;
   private int mark;

   public TokenScanner(GrammarIndexer indexer, String resource, char[] original, char[] source, short[] lines, short[] types) {
      this.indexer = new TokenIndexer(indexer, resource, original, source, lines, types);
      this.tokens = new ArrayList<Token>();
      this.count = source.length;
   }

   @Override
   public Token<String> text() {
      if (masks == null) {
         masks = indexer.index(tokens);
      }
      if (mark < masks.length) {
         if ((masks[mark] & TEXT.mask) != 0) {
            return tokens.get(mark++);
         }
      }
      return null;
   }
   
   @Override
   public Token<String> template() {
      if (masks == null) {
         masks = indexer.index(tokens);
      }
      if (mark < masks.length) {
         if ((masks[mark] & TEMPLATE.mask) != 0) {
            return tokens.get(mark++);
         }
      }
      return null;
   }

   @Override
   public Token<String> type() {
      if (masks == null) {
         masks = indexer.index(tokens);
      }
      if (mark < masks.length) {
         if ((masks[mark] & TYPE.mask) != 0) {
            return tokens.get(mark++);
         }
      }
      return null;
   }

   @Override
   public Token<String> identifier() {
      if (masks == null) {
         masks = indexer.index(tokens);
      }
      if (mark < masks.length) {
         if ((masks[mark] & IDENTIFIER.mask) != 0) {
            return tokens.get(mark++);
         }
      }
      return null;
   }

   @Override
   public Token<String> qualifier() {
      if (masks == null) {
         masks = indexer.index(tokens);
      }
      if (mark < masks.length) {
         if ((masks[mark] & QUALIFIER.mask) != 0) {
            return tokens.get(mark++);
         }
      }
      return null;
   }

   @Override
   public Token<String> literal(String text) {
      if (masks == null) {
         masks = indexer.index(tokens);
      }
      if (mark < masks.length) {
         if ((masks[mark] & LITERAL.mask) != 0) {
            Token token = tokens.get(mark);
            Object value = token.getValue();

            if (value == text) {
               mark++;
               return token;
            }
         }
      }
      return null;
   }
   
   @Override
   public Token<Number> binary() {
      if (masks == null) {
         masks = indexer.index(tokens);
      }
      if (mark < masks.length) {
         if ((masks[mark] & BINARY.mask) != 0) {
            return tokens.get(mark++);
         }
      }
      return null;
   }

   @Override
   public Token<Number> hexidecimal() {
      if (masks == null) {
         masks = indexer.index(tokens);
      }
      if (mark < masks.length) {
         if ((masks[mark] & HEXIDECIMAL.mask) != 0) {
            return tokens.get(mark++);
         }
      }
      return null;
   }

   @Override
   public Token<Number> decimal() {
      if (masks == null) {
         masks = indexer.index(tokens);
      }
      if (mark < masks.length) {
         if ((masks[mark] & DECIMAL.mask) != 0) {
            return tokens.get(mark++);
         }
      }
      return null;
   }
   
   @Override
   public Line line(int position) {
      int length = tokens.size();
      int index = Math.min(length -1 , position);
      Token token = tokens.get(index);
      
      if(token != null) {
         return token.getLine();
      }
      return null;
   }

   @Override
   public int reset(int position) {
      int current = mark;

      if (position <= count || position >= 0) {
         mark = position;
      }
      return current;
   }

   @Override
   public int count() {
      return tokens.size();
   }

   @Override
   public int mark() {
      return mark;
   }
}
