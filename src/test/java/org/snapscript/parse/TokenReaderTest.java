package org.snapscript.parse;

import org.snapscript.parse.TextReader;

import junit.framework.TestCase;

public class TokenReaderTest extends TestCase  {
   public void testX(){}
   /*
   public void testMixText() throws Exception {
      TextReader decoder = new TextReader("'some stuff='+x+\"\\nother stuff\"+'\\nyet more'+x".toCharArray());
      
      assertEquals(decoder.text(), "some stuff=");
      assertTrue(decoder.skip("+x+"));
      assertEquals(decoder.text(), "\nother stuff");
      assertTrue(decoder.skip("+"));
      assertEquals(decoder.text(), "\nyet more");
      assertTrue(decoder.skip("+x"));
      
   }
   public void testSingleQuoteText() throws Exception {      
      TextReader decoder = new TextReader("'hello world'\"middle 'blah' stuff\"'stuff''a\\'b\\'''something\nover\nmany\nlines\\'blah\\''".toCharArray());
      
      assertEquals(decoder.text(), "hello world");
      assertEquals(decoder.text(), "middle 'blah' stuff");
      assertEquals(decoder.text(), "stuff");
      assertEquals(decoder.text(), "a'b'");      
      assertEquals(decoder.text(), "something\nover\nmany\nlines'blah'");        

   }

   public void testQualifier() throws Exception {      
      TextReader decoder = new TextReader("java.lang.*;java.lang.Math.*;java.lang.Math.round".toCharArray());
      
      assertEquals(decoder.qualifier(), "java.lang");
      assertTrue(decoder.skip(".*;"));
      assertEquals(decoder.qualifier(), "java.lang.Math");
      assertTrue(decoder.skip(".*;"));
      assertEquals(decoder.qualifier(), "java.lang.Math.round");        

   }
   
   public void testText() throws Exception {      
      assertEquals(new TextReader(new char[]{'"', '\\', '"', '"'}).text(), new String(new char[]{'"'}));
      assertEquals(new TextReader(new char[]{'"', '\\', 'n', '"'}).text(), new String(new char[]{'\n'}));
      assertEquals(new TextReader(new char[]{'"', '\\', 't', '"'}).text(), new String(new char[]{'\t'}));
      assertEquals(new TextReader(new char[]{'"', '\\', 'f', '"'}).text(), new String(new char[]{'\f'}));      

   }

   public void testComments() throws Exception {
      TextReader decoder = new TextReader("0xff;hello,12.0f/2.0f,\"some string with stuff\\\"blah\\\"d\"".toCharArray());
      
      assertEquals(decoder.hexidecimal(), 255);
      assertEquals(decoder.next(),';');      
      assertEquals(decoder.token(), "hello");
      assertEquals(decoder.next(),',');  
      assertEquals(decoder.decimal(), 12.0f);
      assertEquals(decoder.next(), '/');
      assertEquals(decoder.decimal(), 2.0f);
      assertEquals(decoder.next(), ',');
      assertEquals(decoder.text(),"some string with stuff\"blah\"d"); // not escaped!!!! // some string with stuff \"blah\"d
   }
   
   public void testHex() throws Exception {
      TextReader decoder = new TextReader("0xff".toCharArray());

      assertEquals(decoder.hexidecimal(), 255);              
 
   }
   
   public void testNumbers() throws Exception {
      TextReader decoder1 = new TextReader("1L,20l,11,12.002".toCharArray());
      
      assertEquals(decoder1.integer(), 1L);    
      assertEquals(decoder1.next(), ',');
      assertEquals(decoder1.integer(), 20L);    
      assertEquals(decoder1.next(), ',');
      assertEquals(decoder1.integer(), 11);    
      assertEquals(decoder1.next(), ',');
      assertEquals(decoder1.integer(), 12);  
      
      TextReader decoder2 = new TextReader("1,20,11,12.002,11d,124.0f".toCharArray());
      
      assertEquals(decoder2.integer(), 1);    
      assertEquals(decoder2.next(), ',');
      assertEquals(decoder2.decimal(), 20.0d);    
      assertEquals(decoder2.next(), ',');
      assertEquals(decoder2.integer(), 11);    
      assertEquals(decoder2.next(), ',');
      assertEquals(decoder2.decimal(), 12.002d);
      assertEquals(decoder2.next(), ',');
      assertEquals(decoder2.decimal(), 11d);
      assertEquals(decoder2.next(), ',');
      assertEquals(decoder2.decimal(), 124.0f);       
 
   }
   
   public void testDecoder() throws Exception {
      TextReader decoder = new TextReader("this,is,a,\"simple\",test,12".toCharArray());
      
      assertEquals(decoder.token(), "this");
      assertEquals(decoder.peek(), ',');
      assertEquals(decoder.next(), ',');
      assertEquals(decoder.token(), "is");
      assertEquals(decoder.next(), ',');
      assertEquals(decoder.token(), "a");
      assertEquals(decoder.next(), ',');
      assertEquals(decoder.text(), "simple");
      assertEquals(decoder.next(), ',');
      assertEquals(decoder.token(), "test");
      assertEquals(decoder.next(), ',');
      assertEquals(decoder.integer().intValue(), 12);        
   }

   public void testEscapeDecoder() throws Exception {
      TextReader decoder = new TextReader("\"a \\u0063\\u006c\\u0061\\u0073\\u0073\\u0020\\u0054\\u0065\\u0073\\u0074 714 \"".toCharArray());

      System.err.println(decoder.text());
   }*/
}
