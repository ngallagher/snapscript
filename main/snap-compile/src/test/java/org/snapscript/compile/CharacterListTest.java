package org.snapscript.compile;

import junit.framework.TestCase;

import org.snapscript.compile.instruction.collection.CharacterList;

public class CharacterListTest extends TestCase {

   public void testList() throws Exception {
      char[] array = new char[1024];
      CharacterList list = new CharacterList(array, Character.class);
      
      assertEquals(array.length, list.size());
      
      for(int i = 0; i < array.length; i++) {
         assertEquals(list.get(i), (Character)((char)0));
      }
   }
}
