package org.snapscript.interpret;

import org.snapscript.interpret.PrimitiveNumberList;

import junit.framework.TestCase;

public class PrimitiveNumberListTest extends TestCase {

   public void testList() throws Exception {
      byte[] array = new byte[1024];
      PrimitiveNumberList list = new PrimitiveNumberList(array, Byte.class);
      
      assertEquals(array.length, list.size());
      
      for(int i = 0; i < array.length; i++) {
         assertEquals(list.get(i), (byte)0);
      }
      assertTrue(list.contains("0"));
      Byte[] copy = (Byte[])list.toArray();
      
      assertEquals(copy.length, array.length);
      for(int i = 0; i < array.length; i++) {
         assertEquals(copy[i], (Byte)((byte)0));
      }
      for(int i = 0; i < 100; i++){
         list.set(i, i);
      }
      for(int i = 0; i < 100; i++){
         assertEquals(list.indexOf(String.valueOf(i)), i);
      }
      
   }
}
