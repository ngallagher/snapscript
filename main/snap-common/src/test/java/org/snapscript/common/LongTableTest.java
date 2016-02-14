package org.snapscript.common;

import junit.framework.TestCase;

public class LongTableTest extends TestCase {
   
   public void testComparison() throws Exception {
      
   }
   
   public void testTable() throws Exception {
      long[] array = new long[200];
      LongTable<String> table = new LongTable<String>(array, 50, 100);
      
      table.put(1, "ONE");
      table.put(2, "TWO");
      table.put(3, "THREE");
      
      assertEquals(table.get(1), "ONE");
      assertEquals(table.get(2), "TWO");
      assertEquals(table.get(3), "THREE");
   }

}
