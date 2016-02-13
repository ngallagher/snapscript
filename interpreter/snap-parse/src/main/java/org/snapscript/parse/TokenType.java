package org.snapscript.parse;

public enum TokenType {
   IDENTIFIER(0, 0x0001),
   TYPE(1, 0x0002),   
   QUALIFIER(2, 0x0004),   
   HEXIDECIMAL(3, 0x0008),   
   DECIMAL(4, 0x0010),
   TEXT(5, 0x0020),
   LITERAL(6, 0x0040),
   TEMPLATE(7, 0x0080);
   
   public final int index;
   public final int mask;
   
   private TokenType(int index, int mask) {
      this.index = index;
      this.mask = mask;
   }
}
