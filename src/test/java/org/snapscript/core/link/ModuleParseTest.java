package org.snapscript.core.link;

import java.util.HashSet;
import java.util.Set;

import org.snapscript.parse.LexerBuilder;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

import junit.framework.TestCase;

public class ModuleParseTest extends TestCase {

   public void testParse() throws Exception {
      SyntaxParser tree = LexerBuilder.create();

      assertNotNull(tree);

      analyze(tree, "module x{}", "script");      

          
   }

   private void analyze(SyntaxParser analyzer, String source, String grammar) throws Exception {
      analyze(analyzer, source, grammar, true);
   }

   private void analyze(SyntaxParser analyzer, String source, String grammar, boolean success) throws Exception {
      Set<String> keep=new HashSet<String>();
      
      keep.add("expression");
      keep.add("reference");
      keep.add("method");
      keep.add("variable");
      keep.add("literal");
      keep.add("construct");       
      keep.add("calculation-expression");
      keep.add("calculation-operator");
      keep.add("arithmetic-expression");
      
      SyntaxNode list = analyzer.parse(source, grammar);

      if (list != null) {
         LexerBuilder.print(analyzer, source, grammar);
      }else {
         if(success) {
            assertTrue(false);
         } 
      }
   }

}

