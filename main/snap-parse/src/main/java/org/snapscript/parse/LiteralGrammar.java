package org.snapscript.parse;


public class LiteralGrammar implements Grammar {

   private final Matcher matcher;
   
   public LiteralGrammar(String value) {
      this.matcher = new GrammarMatcher(value);
   }
   
   @Override
   public Matcher compile(int serial) {
      return matcher;
   } 
   
   private static class GrammarMatcher implements Matcher {
      
      private final String value;
      
      public GrammarMatcher(String value) {
         this.value = value;
      }
      
      @Override
      public boolean match(SyntaxReader source, int depth) {
         return source.literal(value);
      }
      
      @Override
      public String toString() {
         return String.format("'%s'", value);
      }
   }
}
