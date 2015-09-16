package org.snapscript.parse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

import junit.framework.TestCase;

public class FunctionParseTest extends TestCase {
   public static void main(String[] l)throws Exception{
      new FunctionParseTest().testParse();
   }

   public void testParse() throws Exception {
      SyntaxParser tree = LexerBuilder.create();

      assertNotNull(tree);

      //analyze(tree, "try{out.println();}catch(e){out.println(e);}", "try-catch-statement");
      //analyzeScript(tree, 1);
      analyze(tree, "var infinity=Float.POSITIVE_INFINITY;", "statement");
      analyze(tree, "class.functions", "reference");
      analyze(tree, "var x={};", "statement");
      analyze(tree, "var x={:};", "statement");
      analyze(tree, "x=i+1;", "statement");
      analyze(tree, "return x=i+1;", "statement");
      analyze(tree, "return;", "statement");
      analyze(tree, "return new Date();", "statement");       
      analyze(tree, "if(i>0)x.callMethod();", "if-statement");
      analyze(tree, "var x = 1;", "declaration-statement");      
      analyze(tree, "if(i>0)x.callMethod();", "script");     
      analyze(tree, "function foo(a,b){return a>b;}", "script-function");
      analyze(tree, "function foo(a,b){return a>b;}function blah(){printf(\"blah\");}", "script");               
      analyze(tree, "for(x in y){x.doIt();}", "script");
      analyze(tree, "for(var x in y){x.doIt();}", "script");
      analyze(tree, "for(var x in [1,2,3]){x.doIt();}", "script");      
      analyze(tree, "return(x);", "return-statement");     
      analyze(tree, "str1 = str1+ student[key];", "script");
      analyze(tree, "var newText = document.createTextNode(demo());","script");
      analyze(tree, "var  key = \"\";", "script");

      analyze(tree, "var x=[a,b,c];", "script");
      analyze(tree, "var x=[\"a\",true,c.doIt()];", "script");
      analyze(tree, "var x={a:true,b:false};", "script");
      analyze(tree, "var i=0;", "declaration-statement");
       
      analyze(tree, "for(var i=0;i<10;i++){x.do();}", "for-statement");
      analyze(tree, "println(\"hello\"+i)", "expression");        
      analyze(tree, "println(\"hello\"+i);", "script");
      analyze(tree, "for(var i=0;i<10;i++){println(\"hello\"+i);}", "script");      
      analyze(tree, "left<mid-1", "comparison");
      analyze(tree, "left<mid-1", "conditional");      
      analyze(tree, "pivot = array[x]", "assignment");
      analyze(tree, "(left + right) >>> 2", "calculation");
      analyze(tree, "pivot = array[(left + right) >>> 1]", "assignment");
      analyze(tree, "array[left] < pivot", "conditional");
      analyze(tree, "++i", "expression");  
      analyze(tree, "while (x < pivot) { i++; }","conditional-statement");
      
      analyze(tree, "++i", "increment-decrement");
      analyze(tree, "i++", "increment-decrement");
      analyze(tree, "--i", "decrement");
      analyze(tree, "for(x in [2,4,6,8]){out.println(x);}", "for-in-statement");
      
      analyze(tree, "import java.util.HashMap;", "import");
      
      analyzeScripts(tree);  
      testParsePerformance(tree);
      analyzeScript(tree, 13);
   }
   
   private String load(File file) throws Exception{      
      FileInputStream in = new FileInputStream(file);
      ByteArrayOutputStream out=new ByteArrayOutputStream();
      byte[] buffer=new byte[1024];
      int count = 0;
      while((count=in.read(buffer))!=-1){
         out.write(buffer,0,count);
      }
      return out.toString();
   }
   
   private void testParsePerformance(SyntaxParser analyzer) throws Exception {
      int iterations = 1;
      for(int i = 0; i < 100;i++) {
         File file = new File("C:\\Work\\development\\github\\snapscript\\src\\test\\java\\org\\snapscript\\parse\\script"+i+".js");  
         if(file.exists()) {
            String source=load(file);
            long start=System.currentTimeMillis();
            long last=start;
            for(int j=0;j<iterations;j++){
               last=System.currentTimeMillis();
               SyntaxNode list = analyzer.parse(source, "script");
               assertNotNull(list);
            }
            long finish=System.currentTimeMillis();
            long duration=finish-start;
            long once=finish-last;
            
            System.err.println("Time taken to parse [script"+i+".js] " +iterations+" times was " + duration + " last was "+once);           
         }
      }
   }

   private void analyzeScripts(SyntaxParser analyzer) throws Exception {
      for(int i = 0; i < 100;i++) {
         File file = new File("C:\\Work\\development\\github\\snapscript\\src\\test\\java\\org\\snapscript\\parse\\script"+i+".js");  
         if(file.exists()) {
            analyze(analyzer,load(file),"script");
         }
      }
   }
   
   private void analyzeScript(SyntaxParser analyzer, int num) throws Exception {
      int iterations = 10;      
      File file = new File("C:\\Work\\development\\github\\snapscript\\src\\test\\java\\org\\snapscript\\parse\\script"+num+".js");  
      if(file.exists()) {
         String source=load(file);
         //LexerBuilder.print(analyzer, source, "script");
         long start=System.currentTimeMillis();
         long last=start;
         for(int j=0;j<iterations;j++){
            last=System.currentTimeMillis();
            SyntaxNode list = analyzer.parse(source, "script");
            assertNotNull(list);
         }
         long finish=System.currentTimeMillis();
         long duration=finish-start;
         long once=finish-last;
         
         System.err.println("Time taken to parse [script"+num+".js] " +iterations+" times was " + duration + " last was "+once);   
      }      
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
      
      long start=System.currentTimeMillis();
      SyntaxNode list = analyzer.parse(source, grammar);
      long finish=System.currentTimeMillis();
      long duration=finish-start;


      assertNotNull(list);

      if (list != null) {
         //String remaining = iterator.ready();
        // if (remaining != null && remaining.length() > 0) {
        //    System.err.println("###################### FAIL TO COMPLETE #### [" + source + "] -> [" + remaining + "] #####");
        // }
         if (!success) {
            assertTrue(false);
         }
         LexerBuilder.print(analyzer, source, grammar);
      }

      
      System.err.println("Time taken to parse was " + duration); 
   }

}

