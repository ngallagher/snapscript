package org.snapscript.parse;

public class TextSource {

   private final char[] source;
   private final short[] lines;
   
   public TextSource(char[] source, short[] lines) {
      this.source = source;
      this.lines = lines;
   }
   
   public char[] getSource() {
      return source;
   }
   
   public short[] getLines() {
      return lines;
   }
}
