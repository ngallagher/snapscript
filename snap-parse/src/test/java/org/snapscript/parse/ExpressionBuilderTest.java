package org.snapscript.parse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import junit.framework.TestCase;

public class ExpressionBuilderTest extends TestCase {

   public void testParse() throws Exception {
      SyntaxParser tree = LexerBuilder.create();

      
      assertNotNull(tree);      
      analyzeScripts(tree);  
      testParsePerformance(tree);
   }   

   private void analyzeScripts(SyntaxParser analyzer) throws Exception {      
      for(int i = 0; i < 100;i++) {
         File file = new File("C:\\Work\\development\\github\\snapscript-builder\\src\\test\\java\\org\\snapscript\\parse\\script"+i+".js");  
         if(file.exists()) {
            String source = load(file);
            LexerBuilder.print(analyzer, source, "script");
            System.err.println();
            System.err.println();
         }
      }
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
      int iterations = 1000;
      for(int i = 0; i < 100;i++) {
         File file = new File("C:\\Work\\development\\github\\snapscript-builder\\src\\test\\java\\org\\snapscript\\parse\\script"+i+".js");  
         if(file.exists()) {
            String source=load(file);
            long start=System.currentTimeMillis();
            long last=start;
            for(int j=0;j<iterations;j++){
               SyntaxNode list=analyzer.parse(source, "script");
               assertNotNull(list);
               last=System.currentTimeMillis();
            }
            long finish=System.currentTimeMillis();
            long duration=finish-start;
            long once=finish-last;
            
            System.err.println("Time taken to parse [script"+i+".js] " +iterations+" times was " + duration + " last was "+once);           
         }
      }
   }
}

