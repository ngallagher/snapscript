package org.snapscript.parse;

import java.util.ArrayList;
import java.util.List;

public class MatchOneGrammar implements Grammar {
   
   private final List<Grammar> grammars;
   private final String name;
   private final int capacity;
   private final int index;
   
   public MatchOneGrammar(List<Grammar> grammars, String name, int index) {
      this(grammars, name, index, 1000);
   }
   
   public MatchOneGrammar(List<Grammar> grammars, String name, int index, int capacity) {
      this.grammars = grammars;
      this.capacity = capacity;
      this.index = index;
      this.name = name;
   }       
   
   @Override
   public GrammarMatcher create(int serial) {
      List<GrammarMatcher> matchers = new ArrayList<GrammarMatcher>();
      
      for(Grammar grammar : grammars) {
         GrammarMatcher matcher = grammar.create(serial);
         matchers.add(matcher);
      }
      return new MatchOneMatcher(matchers, name, index, capacity);
   }
   
   private static class MatchOneMatcher implements GrammarMatcher {
      
      private final PositionCache<GrammarMatcher> cache;
      private final List<GrammarMatcher> matchers;
      private final PositionSet failure;
      private final String name;
      private final int index;

      public MatchOneMatcher(List<GrammarMatcher> matchers, String name, int index, int capacity) {
         this.cache = new PositionCache<GrammarMatcher>(capacity);
         this.failure = new PositionSet(capacity);
         this.matchers = matchers;
         this.index = index;
         this.name = name;
      }    
   
      @Override
      public boolean match(SyntaxBuilder builder, int depth) {
         Integer position = builder.position();
         
         if(!failure.contains(position)) {
            GrammarMatcher best = cache.fetch(position);
            
            if(best == null) {
               int count = matchers.size();
               int size = -1;     
                  
               for(int i = 0; i < count; i++) {
                  GrammarMatcher matcher = matchers.get(i);
                  SyntaxBuilder child = builder.mark(index);   
         
                  if(child != null) {
                     if(matcher.match(child, 0)) {
                        int offset = child.reset();
                        
                        if(offset > size) {
                           size = offset;
                           best = matcher;
                        }
                     } else {
                        child.reset();
                     }
                  }
               }                  
               if(best != null) {
                  cache.cache(position, best);
               } else {
                  failure.add(position);
               }
            }
            if(best != null) {            
               if(!best.match(builder, 0)) {
                  throw new ParseException("Could not read node in " + name);  
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
