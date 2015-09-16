package org.snapscript.parse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchAllGrammar implements Grammar {

   private final List<Grammar> grammars;
   private final Set<Long> success;
   private final Set<Long> failure;
   private final String name;
   private final int index;
   
   public MatchAllGrammar(List<Grammar> grammars, String name, int index) {
      this.success = new HashSet<Long>();
      this.failure = new HashSet<Long>();
      this.grammars = grammars;
      this.index = index;
      this.name = name;
   }  
   
   @Override
   public boolean read(SyntaxReader source, int depth) {
      long position = source.position();
      
      if(depth == 0) {
         for(Grammar grammar : grammars) {               
            if(!grammar.read(source, depth + 1)) {
               return false; 
            }
         }
         return true;
      }
      if(!failure.contains(position)) {
         if(!success.contains(position)) {
            SyntaxReader child = source.mark(index);   
            int require = grammars.size();
            int count = 0;
            
            if(child != null) {            
               for(Grammar grammar : grammars) {               
                  if(!grammar.read(child, 0)) {
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
            for(Grammar grammar : grammars) {               
               if(!grammar.read(source, 0)) {
                  throw new IllegalStateException("Could not read node in " + name);  
               }
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
