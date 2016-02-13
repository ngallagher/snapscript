package org.snapscript.parse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

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
            String source = ClassPathReader.load("/script"+i+".snap");
            if(source != null) {
               LexerBuilder.print(analyzer, source, "script");
               System.err.println();
               System.err.println();
            }
      }
   }
   
   private void testParsePerformance(SyntaxParser analyzer) throws Exception {
      int iterations = 1000;
      for(int i = 0; i < 100;i++) {
         String source = ClassPathReader.load("/script"+i+".snap");
         if(source != null) {
            long start=System.currentTimeMillis();
            long last=start;
            for(int j=0;j<iterations;j++){
               SyntaxNode list=analyzer.parse("/script"+i+".snap",source, "script");
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

