package org.snapscript.parse;

public class SourceCode {

   private final char[] source;
   private final short[] lines;
   private final short[] types;
   
   public SourceCode(char[] source, short[] lines, short[] types) {
      this.source = source;
      this.lines = lines;
      this.types = types;
   }
   
   public char[] getSource() {
      return source;
   }
   
   public short[] getLines() {
      return lines;
   }
   
   public short[] getTypes() {
      return types;
   }
   
   public String toString(){
      return new String(source);
   }
}
