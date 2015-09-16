package org.snapscript.parse;

public class StringToken implements Token<String>{
   
   private final String value;
   private final int type;
   private final int line;
   
   public StringToken(String value) {
      this(value, 0, 0);
   }
   
   public StringToken(String value, int line, int type) {
      this.value = value;
      this.type = type;
      this.line = line;
   }
   
   @Override
   public String getValue() {
      return value;
   }
   
   @Override
   public int getLine() {
      return line;
   }
   
   @Override
   public int getType() {
      return type;
   }
}
