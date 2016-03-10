package org.snapscript.parse;


public class SymbolGrammar implements Grammar {

   private final Matcher matcher;
   
   public SymbolGrammar(Symbol symbol, String value, int index) {
      this.matcher = new SymbolMatcher(symbol, value, index);
   }

   @Override
   public Matcher compile(int serial) {
      return matcher;
   }  
   
   private static class SymbolMatcher implements Matcher {
      
      private final Symbol symbol;
      private final String value;
      private final int index;
      
      public SymbolMatcher(Symbol symbol, String value, int index) {
         this.symbol = symbol;
         this.value = value; 
         this.index = index;
      }
   
      @Override
      public boolean match(SyntaxReader reader, int depth) {
         SyntaxReader child = reader.mark(index);
   
         if(symbol.read(child)) {
            child.commit();
            return true;
         }
         return false;
      }
      
      @Override
      public String toString() {
         return String.format("[%s]", value);
      } 
   }
}
