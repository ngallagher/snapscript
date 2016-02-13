package org.snapscript.parse;

import java.util.List;

public class MatchAllGrammar implements Grammar {

   private final List<Grammar> grammars;
   private final PositionSet success;
   private final PositionSet failure;
   private final String name;
   private final int index;
   
   public MatchAllGrammar(List<Grammar> grammars, String name, int index) {
      this(grammars, name, index, 100);
   }
   
   public MatchAllGrammar(List<Grammar> grammars, String name, int index, int capacity) {
      this.success = new PositionSet(capacity);
      this.failure = new PositionSet(capacity);
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
