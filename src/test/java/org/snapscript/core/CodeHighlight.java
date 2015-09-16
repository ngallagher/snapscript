package org.snapscript.core;

public class CodeHighlight {
   
   public static final String COMMENT = "comment";
   public static final String KEYWORD = "keyword";
   public static final String NORMAL = "normal";
   public static final String STRING = "string";
   public static final String NUMBER = "number";
   
   private final String source;
   private final String style;
   private final int offset;
   private final int length;

   public CodeHighlight(String source, String style, int offset, int length) {
      this.source = source;
      this.offset = offset;
      this.length = length;
      this.style = style;
   }
   
   public int getOffset() {
      return offset;
   }
   
   public int getLength() {
      return length;
   }
   
   public String getStyle() {
      return style;
   }
   
   public String getSource() {
      return source;
   }
   
   public String getCode() {
      return source.substring(offset, offset + length);
   }
}