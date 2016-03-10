package org.snapscript.parse;

import java.util.ArrayList;
import java.util.List;

public class MatchAllGrammar implements Grammar {

   private final List<Grammar> grammars;
   private final String name;
   private final int capacity;
   private final int index;
   
   public MatchAllGrammar(List<Grammar> grammars, String name, int index) {
      this(grammars, name, index, 1000);
   }
   
   public MatchAllGrammar(List<Grammar> grammars, String name, int index, int capacity) {
      this.grammars = grammars;
      this.capacity = capacity;
      this.index = index;
      this.name = name;
   }  

   @Override
   public Matcher compile(int serial) {
      List<Matcher> matchers = new ArrayList<Matcher>();
      
      for(Grammar grammar : grammars) {
         Matcher matcher = grammar.compile(serial);
         matchers.add(matcher);
      }
      return new GrammarMatcher(matchers, name, index, capacity);
   } 
   
   public static class GrammarMatcher implements Matcher {
      
      private final List<Matcher> matchers;
      private final PositionSet success;
      private final PositionSet failure;
      private final String name;
      private final int index;

      public GrammarMatcher(List<Matcher> matchers, String name, int index, int capacity) {
         this.success = new PositionSet(capacity);
         this.failure = new PositionSet(capacity);
         this.matchers = matchers;
         this.index = index;
         this.name = name;
      }
   
      @Override
      public boolean match(SyntaxReader source, int depth) {
         Integer position = source.position();
         
         if(depth == 0) {
            for(Matcher matcher : matchers) {               
               if(!matcher.match(source, depth + 1)) {
                  return false; 
               }
            }
            return true;
         }
         if(!failure.contains(position)) {
            if(!success.contains(position)) {
               SyntaxReader child = source.mark(index);   
               int require = matchers.size();
               int count = 0;
               
               if(child != null) {            
                  for(Matcher grammar : matchers) {               
                     if(!grammar.match(child, 0)) {
                        failure.add(position);
                        break;
                     }
                     count++;
                  }
                  child.reset();
               }           
               if(count == require) {
                  success.add(position);
               }
            }
            if(success.contains(position)) {
               for(Matcher grammar : matchers) {               
                  if(!grammar.match(source, 0)) {
                     throw new ParseException("Could not read node in " + name);  
                  }
               }
               return true;
            } 
         }
         return false;
      }
      
      @Override
      public String toString() {
         return String.valueOf(matchers);
      }
   }
}
