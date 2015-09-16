package org.snapscript.parse;

import org.snapscript.parse.TextCompressor;

import junit.framework.TestCase;

public class CompressorTest extends TestCase{
   public void testCompressor() throws Exception{
      TextCompressor compressor = new TextCompressor();
      char[] result1 = compressor.compress("'some stuff='+x + \"\\nother stuff\" + '\\nyet more'+x").getSource();
      String text1 = new String(result1);
      
      assertEquals(text1, "'some stuff='+x+\"\\nother stuff\"+'\\nyet more'+x");
      
      char[] result2 = compressor.compress("'some stuff='+x+\"\\nother stuff\"+'\\nyet more'+x").getSource();
      String text2 = new String(result2);
      
      assertEquals(text2, "'some stuff='+x+\"\\nother stuff\"+'\\nyet more'+x");
   }

}
