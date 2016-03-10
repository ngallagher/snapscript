package org.snapscript.parse;


public class OptionalGrammar implements Grammar {

   private final Grammar grammar; 
   
   public OptionalGrammar(Grammar grammar) {
      this.grammar = grammar;
   }
   
   @Override
   public Matcher compile(int serial) {
      Matcher matcher = grammar.compile(serial);
      return new OptionalMatcher(matcher);
   } 
   
   private static class OptionalMatcher implements Matcher {
      
      private final Matcher matcher;
      
      public OptionalMatcher(Matcher matcher) {
         this.matcher = matcher; 
      }
   
      @Override
      public boolean match(SyntaxReader reader, int depth) {      
         matcher.match(reader, depth);
         return true;
      }
      
      @Override
      public String toString() {
         return String.format("?%s", matcher);
      }    
   }
}

