package org.snapscript.parse;

import java.util.List;

public class MatchFirstGrammar implements Grammar {
   
   private final PositionCache<Grammar> cache;
   private final List<Grammar> grammars;
   private final PositionSet failure;
   private final String name;
   
   public MatchFirstGrammar(List<Grammar> grammars, String name) {
      this(grammars, name, 100);
   }
   
   public MatchFirstGrammar(List<Grammar> grammars, String name, int capacity) {
      this.cache = new PositionCache<Grammar>(capacity);
      this.failure = new PositionSet(capacity);
      this.grammars = grammars;
      this.name = name;
   }       
   
   @Override
   public boolean read(SyntaxReader node, int depth) {
      long position = node.position();
      
      if(!failure.contains(position)) {
         Grammar best = cache.fetch(position);
         
         if(best == null) {
            int count = grammars.size();    
               
            for(int i = 0; i < count; i++) {
               Grammar grammar = grammars.get(i);   
      
               if(grammar.read(node, depth + 1)) {
                  cache.cache(position, grammar);
                  return true;
               }               
            }                  
            failure.add(position);            
         }
         if(best != null) {            
            if(!best.read(node, 0)) {
               throw new IllegalStateException("Could not read node in " + name);  
            }     
            return true;
         }      
      }
      return false;
   }
   
   @Override
   public String toString() {
      return String.format("{%s}", grammars);
   }    
}
