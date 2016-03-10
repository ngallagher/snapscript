package org.snapscript.parse;

import java.util.ArrayList;
import java.util.List;

public class MatchFirstGrammar implements Grammar {
   
   private final List<Grammar> grammars;
   private final String name;
   private final int capacity;
   
   public MatchFirstGrammar(List<Grammar> grammars, String name) {
      this(grammars, name, 1000);
   }
   
   public MatchFirstGrammar(List<Grammar> grammars, String name, int capacity) {
      this.grammars = grammars;
      this.capacity = capacity;
      this.name = name;
   }       

   @Override
   public GrammarMatcher create(int serial) {
      List<GrammarMatcher> matchers = new ArrayList<GrammarMatcher>();
      
      for(Grammar grammar : grammars) {
         GrammarMatcher matcher = grammar.create(serial);
         matchers.add(matcher);
      }
      return new MatchFirstMatcher(matchers, name, capacity);
   } 
   
   private static class MatchFirstMatcher implements GrammarMatcher {
      
      private final PositionCache<GrammarMatcher> cache;
      private final List<GrammarMatcher> matchers;
      private final PositionSet failure;
      private final String name;
      
      public MatchFirstMatcher(List<GrammarMatcher> matchers, String name, int capacity) {
         this.cache = new PositionCache<GrammarMatcher>(capacity);
         this.failure = new PositionSet(capacity);
         this.matchers = matchers;
         this.name = name;
      }    
   
      @Override
      public boolean match(SyntaxBuilder builder, int depth) {
         Integer position = builder.position();
         
         if(!failure.contains(position)) {
            GrammarMatcher best = cache.fetch(position);
            
            if(best == null) {
               int count = matchers.size();    
                  
               for(int i = 0; i < count; i++) {
                  GrammarMatcher matcher = matchers.get(i);   
         
                  if(matcher.match(builder, depth + 1)) {
                     cache.cache(position, matcher);
                     return true;
                  }               
               }                  
               failure.add(position);            
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
         return String.format("{%s}", matchers);
      }    
   }
}
