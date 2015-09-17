package org.snapscript.parse;

public class RepeatGrammar implements Grammar {

   private final Grammar grammar;  
   private final String name;
   private final boolean once;
   
   public RepeatGrammar(Grammar grammar, String name) {
      this(grammar, name, false);
   }
   
   public RepeatGrammar(Grammar grammar, String name, boolean once) {
      this.grammar = grammar;
      this.name = name;    
      this.once = once;
   }    
   
   @Override
   public boolean read(SyntaxReader reader, int depth) {    
      int count = 0;

      while(true) {   
         if(!grammar.read(reader, depth)) {            
            break;               
         }      
         count++;
      }
      if(once) {
         return count > 0;
      }
      return true;
   }
   
   @Override
   public String toString() {
      return String.format("%s%s", once ? "+" : "*", grammar);
   }     
}

