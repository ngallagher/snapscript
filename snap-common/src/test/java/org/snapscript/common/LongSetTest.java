package org.snapscript.common;

import junit.framework.TestCase;

public class LongSetTest extends TestCase {

   public void testLongSet() throws Exception {
      long[] table = new long[10];
      LongSet set = new LongSet(table, 5, 5);
      
      assertTrue(set.add(3));
      assertTrue(set.add(55));
      assertTrue(set.add(44));
      assertTrue(set.add(82));
      assertFalse(set.add(82));
      assertEquals(set.size(), 4);
      assertTrue(set.add(1342));
      assertEquals(set.size(), 5);
      assertFalse(set.add(4432)); // already full
      assertFalse(set.add(123)); // already full
      assertEquals(set.size(), 5);
      
      assertEquals(set.size(), 5);
      assertTrue(set.contains(3));
      assertTrue(set.contains(55));
      assertTrue(set.contains(44));
      assertTrue(set.contains(82));
      assertFalse(set.contains(83));
      assertFalse(set.contains(41));
      
      assertTrue(set.remove(3));
      assertEquals(set.size(), 4);
   }
}
