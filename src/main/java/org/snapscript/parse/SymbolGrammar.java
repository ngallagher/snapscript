package org.snapscript.parse;

public class SymbolGrammar implements Grammar {

   private final Symbol symbol;
   private final String name;
   private final String value;
   private final int index;
   
   public SymbolGrammar(Symbol symbol, String value, String name, int index) {
      this.symbol = symbol;
      this.value = value; 
      this.index = index;
      this.name = name;
   }
   
   @Override
   public boolean read(SyntaxReader reader, int depth) {
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
