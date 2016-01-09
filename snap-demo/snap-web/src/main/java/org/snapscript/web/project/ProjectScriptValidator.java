package org.snapscript.web.project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class ProjectScriptValidator {

   private final Pattern pattern;
   
   public ProjectScriptValidator() {
      this.pattern = Pattern.compile(".*line\\s+(\\d+)");
   }
   
   public int parse(String resource, String source) {
      try {
         SyntaxCompiler compiler = new SyntaxCompiler();
         SyntaxParser parser = compiler.compile();
         SyntaxNode node = parser.parse(resource, source, "script");
         node.getNodes();
      }catch(Exception e) {
         String message = e.getMessage();
         Matcher matcher = pattern.matcher(message);
         
         if(matcher.matches()) {
            String line = matcher.group(1);
            return Integer.parseInt(line);
         }
         throw new RuntimeException("Script validation error for '"+resource+"'", e);
      }
      return -1;
   }
}
