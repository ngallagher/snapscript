package org.snapscript.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class CodeHighlighterTest extends TestCase {
   
   private static final Map<String, String> STYLE_WORDS = new LinkedHashMap<String, String>();

   static {
      STYLE_WORDS.put("public", "keyword");
      STYLE_WORDS.put("class", "keyword");
      STYLE_WORDS.put("trait", "keyword");
      STYLE_WORDS.put("function", "keyword");
      STYLE_WORDS.put("abstract", "keyword");
      STYLE_WORDS.put("return", "keyword");
      STYLE_WORDS.put("new", "keyword");
      STYLE_WORDS.put("var", "keyword");
      STYLE_WORDS.put("this", "keyword");
      STYLE_WORDS.put("super", "keyword");
      STYLE_WORDS.put("if", "keyword");
      STYLE_WORDS.put("else", "keyword");
      STYLE_WORDS.put("break", "keyword");
      STYLE_WORDS.put("continue", "keyword");
      STYLE_WORDS.put("while", "keyword");
      STYLE_WORDS.put("for", "keyword");
      STYLE_WORDS.put("static", "keyword");
      STYLE_WORDS.put("const", "keyword");
      STYLE_WORDS.put("import", "keyword");
      STYLE_WORDS.put("throw", "keyword");
      STYLE_WORDS.put("extends", "keyword");
      STYLE_WORDS.put("try", "keyword");
      STYLE_WORDS.put("catch", "keyword");
      STYLE_WORDS.put("finally", "keyword");
      STYLE_WORDS.put("in", "keyword");
   }
   
   private static final String SOURCE_1 = 
   "class Test{\n" +
   "  var x;\n" +
   "  var y;\n" +
   "\n" +
   "  new(){\n" +
   "     this.x=x;\n" +
   "  }\n" +
   "  fo(){\n" +
   "     out.println(\"fun\");\n" +
   "  }\n" +
   "  foo(){\n" +
   "     return x;\n" +
   "  }\n" +
   "  x(){\n" +
   "     x++;\n" +
   "  }\n" +
   "}\n" +
   "out.println(\"TODO: fix class declaration\");\n";

   private static final String SOURCE_2 =
   "try {\n"+
   "   throw \"this is some text 1 - catch String\";\n"+
   "}catch(e: String){\n"+
   "   out.println(e);\n"+
   "}\n"+
   "try {\n"+
   "   try {\n"+
   "      throw \"this is some text 2 - catch Integer\";\n"+
   "   }catch(e: Integer){\n"+
   "      out.println(\"problem!!!! \"+e);\n"+
   "   }\n"+
   "}catch(e){\n"+
   "   out.println(\"caught in last block \"+e);\n"+
   "}\n";


//   public void testCodeHighlighter() throws Exception {
//      CodeHighlighter highligher = new CodeHighlighter(STYLE_WORDS);
//      StringBuilder builder = new StringBuilder();
//      
//      List<CodeHighlight> highlights = highligher.createHighlights(SOURCE_1);
//      
//      for(CodeHighlight highlight : highlights) {
//         String code = highlight.getCode();
//         builder.append(code);
//         System.err.print(">>" + code + "<<");
//      }
//      String result = builder.toString();
//      
//      assertEquals(result, SOURCE_1);
//   }
   
   public void testStringCodeHighlighter() throws Exception {
      CodeHighlighter highligher = new CodeHighlighter(STYLE_WORDS);
      StringBuilder builder = new StringBuilder();
      
      List<CodeHighlight> highlights = highligher.createHighlights(SOURCE_2);
      
      for(CodeHighlight highlight : highlights) {
         String code = highlight.getCode();
         builder.append(code);
         System.err.print(">>" + code + "<<");
      }
      String result = builder.toString();
      
      assertEquals(result, SOURCE_2);
   }
}
