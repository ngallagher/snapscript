package org.snapscript.parse;

import java.util.List;

public class MatchOneGrammar implements Grammar {
   
   private final PositionCache<Grammar> cache;
   private final List<Grammar> grammars;
   private final PositionSet failure;
   private final String name;
   private final int index;
   
   public MatchOneGrammar(List<Grammar> grammars, String name, int index) {
      this(grammars, name, index, 100);
   }
   
   public MatchOneGrammar(List<Grammar> grammars, String name, int index, int capacity) {
      this.cache = new PositionCache<Grammar>(capacity);
      this.failure = new PositionSet(capacity);
      this.grammars = grammars;
      this.index = index;
      this.name = name;
   }       
   
   @Override
   public boolean read(SyntaxReader reader, int depth) {
      long position = reader.position();
      
      if(!failure.contains(position)) {
         Grammar best = cache.fetch(position);
         
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
               cache.cache(position, best);
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
