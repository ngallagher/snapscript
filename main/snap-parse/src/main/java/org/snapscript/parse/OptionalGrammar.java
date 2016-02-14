package org.snapscript.parse;

public class OptionalGrammar implements Grammar {

   private final Grammar grammar;  
   private final String name;
   
   public OptionalGrammar(Grammar grammar, String name) {
      this.grammar = grammar;
      this.name = name;   
   }  
   
   @Override
   public boolean read(SyntaxReader reader, int depth) {      
      grammar.read(reader, depth);
      return true;
   }
   
   @Override
   public String toString() {
      return String.format("?%s", grammar);
   }     
}

