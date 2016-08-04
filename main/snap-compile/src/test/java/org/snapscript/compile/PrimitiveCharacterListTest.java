package org.snapscript.compile;

import junit.framework.TestCase;

import org.snapscript.tree.collection.PrimitiveCharacterList;

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
