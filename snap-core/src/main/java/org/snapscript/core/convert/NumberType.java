package org.snapscript.core.convert;

public enum NumberType {
   DECIMAL,
   HEXIDECIMAL,
   NONE;
   
   public boolean isDecimal() {
      return this == DECIMAL;
   }
   
   public boolean isHexidecimal() {
      return this == HEXIDECIMAL;
   }
}
