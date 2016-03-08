package org.snapscript.compile;

import junit.framework.TestCase;

import org.snapscript.compile.instruction.collection.PrimitiveIntegerList;

public class PrimitiveIntegerListTest extends TestCase {

   public void testList() throws Exception {
      int[] array = new int[1024];
      PrimitiveIntegerList list = new PrimitiveIntegerList(array);
      
      assertEquals(array.length, list.size());
      
      for(int i = 0; i < array.length; i++) {
         assertEquals(list.get(i), (int)0);
      }
      assertTrue(list.contains("0"));
      Integer[] copy = (Integer[])list.toArray();
      
      assertEquals(copy.length, array.length);
      for(int i = 0; i < array.length; i++) {
         assertEquals(copy[i], (Integer)((int)0));
      }
      for(int i = 0; i < 100; i++){
         list.set(i, i);
      }
      for(int i = 0; i < 100; i++){
         assertEquals(list.indexOf(String.valueOf(i)), i);
      }
      
   }
}
