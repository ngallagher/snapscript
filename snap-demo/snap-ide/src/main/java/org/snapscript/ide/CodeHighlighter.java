package org.snapscript.ide;

import static org.snapscript.ide.CodeHighlight.COMMENT;
import static org.snapscript.ide.CodeHighlight.NORMAL;
import static org.snapscript.ide.CodeHighlight.STRING;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.snapscript.common.LeastRecentlyUsedMap;

public class CodeHighlighter {
   
   private final Map<String, List<CodeHighlight>> cache;
   private final Map<String, String> styles;
   
   public CodeHighlighter(Map<String, String> styles) {
      this.cache = new LeastRecentlyUsedMap<String, List<CodeHighlight>>();
      this.styles = styles;
   }

   public List<CodeHighlight> createHighlights(String text, HeatMap map) {
      List<CodeHighlight> highlights = cache.get(text);
      // not really working
      //map.setHeat(2, 10);
      char[] data = text.toCharArray();
      short[] lines = new short[data.length];
      short count = 1;
      
      for(int i = 0; i < data.length; i++) {
         char next = data[i];
         lines[i]=count;
         if(next == '\n') {
            count++;
         }
      }
      if(highlights == null) {
         List<CodeHighlight> list = new ArrayList<CodeHighlight>();
         int length = text.length();
         int pos = 0;
         int prev = 0;
         int skip = 0;
         // Search for pattern
         // see I have updated now its not case sensitive
         while (pos < length) {
            CodeHighlight highlight = checkHighlight(text, map, lines[pos], pos);

            if (highlight != null) {
               if (prev < pos) {
                  list.add(new CodeHighlight(map, text, NORMAL, lines[prev], prev, pos - prev));
               }
               list.add(highlight);
               pos = highlight.getOffset() + highlight.getLength();
               prev = pos;
               skip = 0;
            } else {
               skip++;
               pos++;
            }
         }
         if (prev < pos) {
            list.add(new CodeHighlight(map, text, NORMAL, -1, prev, pos - prev));
         }
         cache.put(text, list);
         return list;
      }
      return highlights;
   }

   private CodeHighlight checkHighlight(String text, HeatMap map, final int line, final int pos) {
      int length = text.length();
      
      //System.err.println(text.substring(pos).replace("\n", "[\\n]").replaceAll("\r",  "[\\r]").replace("\t", "[\\t]"));

      if(text.startsWith("\'", pos)) {
         for(int i = pos + 1; i < length; i++) {
            char next = text.charAt(i);
            
            if(next == '\'') {
               char prev = text.charAt(i - 1);
               
               if(prev != '\\') {
                  return new CodeHighlight(map, text, STRING, line, pos, (i - pos) + 1);
               } else {
                  if(i - 2 > 0) {
                     char escape = text.charAt(i - 2);
                     
                     if(escape == '\\') {
                        return new CodeHighlight(map, text, STRING, line, pos, (i - pos) + 1);
                     }
                  }
               }
            }
         }
         return new CodeHighlight(map, text, STRING, line, pos, length - pos);
      }
      if(text.startsWith("\"", pos)) {
         for(int i = pos + 1; i < length; i++) {
            char next = text.charAt(i);
            
            if(next == '\"') {
               char prev = text.charAt(i - 1);
               
               if(prev != '\\') {
                  return new CodeHighlight(map, text, STRING, line, pos, (i - pos) + 1);
               } else {
                  if(i - 2 > 0) {
                     char escape = text.charAt(i - 2);
                     
                     if(escape == '\\') {
                        return new CodeHighlight(map, text, STRING, line, pos, (i - pos) + 1);
                     }
                  }
               }
            }
         }
         return new CodeHighlight(map, text, STRING, line, pos, length - pos);
      }
      if (text.startsWith("//", pos)) {
         int last = text.indexOf("\n", pos);

         if (last == -1) {
            return new CodeHighlight(map, text, COMMENT, line, pos, length - pos);
         }
         return new CodeHighlight(map, text, COMMENT, line, pos, last - pos);
      }
      if (text.startsWith("/*", pos)) {
         int last = text.indexOf("*/", pos);

         if (last == -1) {
            return new CodeHighlight(map, text, COMMENT, line, pos, length - pos);
         }
         return new CodeHighlight(map, text, COMMENT, line, pos, (last + 2)  - pos);
      }
      Set<String> keys = styles.keySet();
      
      for (String key : keys) {
         int size = key.length();

         if (text.startsWith(key, pos)) {
            String style = styles.get(key);
            
            if (pos > 0) {
               char prev = text.charAt(pos - 1);

               if (Character.isLetter(prev) || Character.isDigit(prev)) {
                  continue;
               }
            }
            if (size + pos + 1 < length) {
               char next = text.charAt(size + pos);

               if (Character.isLetter(next) || Character.isDigit(next)) {
                  continue;
               }
            }
            return new CodeHighlight(map, text, style, line, pos, size);
         }
      }
      return null;
   }
}