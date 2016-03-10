package org.snapscript.parse;


public class RepeatGrammar implements Grammar {

   private final Grammar grammar;
   private final boolean once;
   
   public RepeatGrammar(Grammar grammar) {
      this(grammar, false);
   }
   
   public RepeatGrammar(Grammar grammar, boolean once) {
      this.grammar = grammar; 
      this.once = once;
   }    
   
   @Override
   public Matcher compile(int serial) {
      Matcher matcher = grammar.compile(serial);
      return new RepeatMatcher(matcher, once);
   }     

   private static class RepeatMatcher implements Matcher {

      private final Matcher matcher;  
      private final boolean once;

      public RepeatMatcher(Matcher matcher, boolean once) {
         this.matcher = matcher;   
         this.once = once;
      } 
   
      @Override
      public boolean match(SyntaxReader reader, int depth) {    
         int count = 0;
   
         while(true) {   
            if(!matcher.match(reader, depth)) {            
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
         return String.format("%s%s", once ? "+" : "*", matcher);
      }
   }
}

