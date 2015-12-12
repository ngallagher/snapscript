package org.snapscript.interpret.console;

public class CodeHighlight {
   
   public static final String COMMENT = "comment";
   public static final String KEYWORD = "keyword";
   public static final String NORMAL = "normal";
   public static final String STRING = "string";
   public static final String NUMBER = "number";
   
   private final HeatMap map;
   private final String source;
   private final String style;
   private final int offset;
   private final int length;
   private final int line;
   
   public CodeHighlight(HeatMap map, String source, String style, int line, int offset, int length) {
      this.source = source;
      this.offset = offset;
      this.length = length;
      this.style = style;
      this.map = map;
      this.line = line;
   }
   
   public int getLine() {
      return line;
   }
   
   public int getOffset() {
      return offset;
   }
   
   public int getLength() {
      return length;
   }
   
   public String getStyle() {
      return style + map.getHeat(line);
   }
   
   public String getSource() {
      return source;
   }
   
   public String getCode() {
      return source.substring(offset, offset + length);
   }
}