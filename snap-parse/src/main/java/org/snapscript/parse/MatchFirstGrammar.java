package org.snapscript.parse;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.snapscript.common.LeastRecentlyUsedMap;

public class MatchFirstGrammar implements Grammar {
   
   private final Map<Long, Grammar> cache;
   private final List<Grammar> grammars;
   private final Set<Long> failure;
   private final String name;
   
   public MatchFirstGrammar(List<Grammar> grammars, String name) {
      this(grammars, name, 1000);
   }
   
   public MatchFirstGrammar(List<Grammar> grammars, String name, int capacity) {
      this.cache = new LeastRecentlyUsedMap<Long, Grammar>(capacity);
      this.failure = new HashSet<Long>();
      this.grammars = grammars;
      this.name = name;
   }       
   
   @Override
   public boolean read(SyntaxReader node, int depth) {
      long position = node.position();
      
      if(!failure.contains(position)) {
         Grammar best = cache.get(position);
         
         if(best == null) {
            int count = grammars.size();    
               
            for(int i = 0; i < count; i++) {
               Grammar grammar = grammars.get(i);   
      
               if(grammar.read(node, depth + 1)) {
                  cache.put(position, grammar);
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
