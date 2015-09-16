package org.snapscript.parse;

public class LiteralGrammar implements Grammar {

   private final String value;
   private final String name;
   
   public LiteralGrammar(String value, String name) {
      this.value = value;
      this.name = name;
   }
   
   @Override
   public boolean read(SyntaxReader source, int depth) {
      return source.literal(value);
   }
   
   @Override
   public String toString() {
      return String.format("'%s'", value);
   }   
}
