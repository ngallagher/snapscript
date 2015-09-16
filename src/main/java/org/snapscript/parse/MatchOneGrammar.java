package org.snapscript.parse;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.snapscript.common.LeastRecentlyUsedMap;

public class MatchOneGrammar implements Grammar {
   
   private final Map<Long, Grammar> cache;
   private final List<Grammar> grammars;
   private final Set<Long> failure;
   private final String name;
   private final int index;
   
   public MatchOneGrammar(List<Grammar> grammars, String name, int index) {
      this(grammars, name, index, 1000);
   }
   
   public MatchOneGrammar(List<Grammar> grammars, String name, int index, int capacity) {
      this.cache = new LeastRecentlyUsedMap<Long, Grammar>(capacity);
      this.failure = new HashSet<Long>();
      this.grammars = grammars;
      this.index = index;
      this.name = name;
   }       
   
   public boolean accept(SyntaxReader reader, int depth) {
      return false;
   }
   
   @Override
   public boolean read(SyntaxReader reader, int depth) {
      long position = reader.position();
      
      if(!failure.contains(position)) {
         Grammar best = cache.get(position);
         
         if(best == null) {
            int count = grammars.size();
            int size = -1;     
               
            for(int i = 0; i < count; i++) {
               Grammar grammar = grammars.get(i);
               SyntaxReader child = reader.mark(index);   
      
               if(child != null) {
                  if(grammar.read(child, 0)) {
                     int offset = child.reset();
                     
                     if(offset > size) {
                        size = offset;
                        best = grammar;
                     }
                  } else {
                     child.reset();
                  }
               }
            }                  
            if(best != null) {
               cache.put(position, best);
            } else {
               failure.add(position);
            }
         }
         if(best != null) {            
            if(!best.read(reader, 0)) {
               throw new IllegalStateException("Could not read node in " + name);  
            }     
            return true;
         }      
      }
      return false;
   }
   
   @Override
   public String toString() {
      return String.valueOf(grammars);
   }    
}
