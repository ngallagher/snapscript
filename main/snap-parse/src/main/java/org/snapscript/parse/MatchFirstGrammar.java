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
   public Matcher compile(int serial) {
      List<Matcher> matchers = new ArrayList<Matcher>();
      
      for(Grammar grammar : grammars) {
         Matcher matcher = grammar.compile(serial);
         matchers.add(matcher);
      }
      return new MatchFirstMatcher(matchers, name, capacity);
   } 
   
   private static class MatchFirstMatcher implements Matcher {
      
      private final PositionCache<Matcher> cache;
      private final List<Matcher> matchers;
      private final PositionSet failure;
      private final String name;
      
      public MatchFirstMatcher(List<Matcher> matchers, String name, int capacity) {
         this.cache = new PositionCache<Matcher>(capacity);
         this.failure = new PositionSet(capacity);
         this.matchers = matchers;
         this.name = name;
      }    
   
      @Override
      public boolean match(SyntaxReader node, int depth) {
         Integer position = node.position();
         
         if(!failure.contains(position)) {
            Matcher best = cache.fetch(position);
            
            if(best == null) {
               int count = matchers.size();    
                  
               for(int i = 0; i < count; i++) {
                  Matcher matcher = matchers.get(i);   
         
                  if(matcher.match(node, depth + 1)) {
                     cache.cache(position, matcher);
                     return true;
                  }               
               }                  
               failure.add(position);            
            }
            if(best != null) {            
               if(!best.match(node, 0)) {
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
