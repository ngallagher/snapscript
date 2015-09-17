package org.snapscript.core;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import junit.framework.TestCase;

public class SortTest extends TestCase {
   
   private static final int ENTRIES = 1000000;
   private static final Integer[] VALUES = new Integer[ENTRIES];
   
   public void testQuickVersusHeap() throws Exception {
//      Random random = new SecureRandom();
//      for(int i = 0; i < ENTRIES; i++){
//         VALUES[i] = random.nextInt(1000000000);
//      }
//      for(int x = 0; x < 20; x++) {
//         long start1 = System.currentTimeMillis();
//         List<Integer> list = new ArrayList<Integer>();
//         
//         for(int i = 0; i < ENTRIES; i++){
//            list.add(VALUES[i]);
//         }
//         Collections.sort(list);
//         for(int i = 0; i < 10; i++) {
//            list.get(i);
//         }
//         long finish1 = System.currentTimeMillis();
//         long duration1 = finish1 - start1;
//         
//         long start2 = System.currentTimeMillis();
//         PriorityQueue queue = new PriorityQueue();
//         
//         for(int i = 0; i < ENTRIES; i++) {
//            queue.offer(VALUES[i]);
//         }
//         for(int i = 0; i < 10; i++) {
//            queue.poll();
//         }
//         long finish2 = System.currentTimeMillis();
//         long duration2 = finish2 - start2;
//       
//         System.err.println("time1="+duration1+" time2="+duration2);
//      }
      
   }

}
