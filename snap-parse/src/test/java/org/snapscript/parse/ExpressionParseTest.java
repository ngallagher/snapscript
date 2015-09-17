package org.snapscript.parse;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class ExpressionParseTest extends TestCase {

   public void testParse() throws Exception {
      SyntaxParser tree = LexerBuilder.create();

      assertNotNull(tree);

      analyze(tree, "call()", "expression");      
      analyze(tree, "call(12)", "expression");
      analyze(tree, "call(12.0f)", "expression");   
      analyze(tree, "call(\"12\")", "expression");   
      analyze(tree, "call(a[12])", "expression");   
      analyze(tree, "call().another()", "expression");
      analyze(tree, "call().another()", "expression");  
      analyze(tree, "call(12.0f).another(\"text\")", "expression");
      analyze(tree, "document.getElementById(\"demo\")", "expression");
      analyze(tree, "document.innerHTML", "expression"); // too long of a match???? i.e it matches primary           
      //analyze(tree, "document.innerHTML", "indirect-reference"); // too long of a match???? i.e it matches primary            
      analyze(tree, "document.getElementById(\"demo\").innerHTML", "expression");
      analyze(tree, "document.getElementById(\"demo\").innerHTML=z", "expression");
      analyze(tree, "calc.sumValues(1,3,Math.max(2,g.time))", "expression");
      analyze(tree, "a.b(C.D(\" This is a [] trickey index \\\"ok\\\"\"), \"some text\",E.F.G(H.I(\"4\")))", "expression");
      analyze(tree, "method.invoke(i++, \"some text\",  g.doIt())", "expression");
      analyze(tree, "show(x)", "expression");      
      analyze(tree, "show(new Date(1,2,3))", "expression");
      analyze(tree, "x=new StringBuilder()", "expression");
      analyze(tree, "new StringBuilder(1)", "construct");
      analyze(tree, "new StringBuilder()", "construct");      
      analyze(tree, "show(1)", "expression");
      analyze(tree, "show(new Date(1,2,3),1+2, \"some text\")", "expression");
      analyze(tree, "x+=show(new Date(1,2,3),1+2, \"some text\", true, false)", "expression");
      analyze(tree, "new Date(1,2,3).doSomeWork()", "expression");
      analyze(tree, "return i;", "statement");
      analyze(tree, "return new Value(\"blah\");", "statement");        
      analyze(tree, "return new Value(\"blah\").doWork();", "statement");
      analyze(tree, "return i+(x/d);", "statement");
      analyze(tree, "return;", "statement");
      analyze(tree, "return++i;", "statement");
      analyze(tree, "t", "expression");
      analyze(tree, "t!=null", "expression");
      analyze(tree, "i>2", "expression");
      analyze(tree, "a&&y", "expression");
      analyze(tree, "a&&(y)", "expression");
      analyze(tree, "i", "conditional-operand");
      analyze(tree, "i", "comparison-operand");
      analyze(tree, "32", "comparison-operand");
      analyze(tree, "true", "comparison-operand");       
      analyze(tree, "i>32", "comparison");
      analyze(tree, "i>32", "conditional-operand");       
      analyze(tree, "(i>32)&&true", "expression");       
      analyze(tree, "i>32&&true", "expression"); 
      analyze(tree, "i>2&&t!=null", "expression");
      analyze(tree, "i>2&&(t!=null||i==3)", "expression");
      analyze(tree, "throw e;", "throw-statement");
          
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

