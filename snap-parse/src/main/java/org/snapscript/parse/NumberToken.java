package org.snapscript.parse;

public class NumberToken implements Token<Number> {
   
   private final Number value;
   private final int type;
   private final int line;
   
   public NumberToken(Number value) {
      this(value, 0, 0);
   }
   
   public NumberToken(Number value, int line, int type) {
      this.value = value;
      this.type = type;
      this.line = line;
   }
   
   @Override
   public Number getValue() {
      return value;
   }
   
   @Override
   public int getLine(){ 
      return line;
   }
   
   @Override
   public int getType() {
      return type;
   }
}
