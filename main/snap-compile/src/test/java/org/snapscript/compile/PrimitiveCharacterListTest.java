package org.snapscript.compile;

import org.snapscript.compile.collection.PrimitiveCharacterList;

import junit.framework.TestCase;

public class PrimitiveCharacterListTest extends TestCase {

   public void testList() throws Exception {
      char[] array = new char[1024];
      PrimitiveCharacterList list = new PrimitiveCharacterList(array);
      
      assertEquals(array.length, list.size());
      
      for(int i = 0; i < array.length; i++) {
         assertEquals(list.get(i), (Character)((char)0));
      }
   }
}
