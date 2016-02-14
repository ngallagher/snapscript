package org.snapscript.common;

import java.security.SecureRandom;
import java.util.Random;

import junit.framework.TestCase;

public class LongQueueTest extends TestCase {
   
   public void testQueueScroll() throws Exception {
      long[] array = new long[100];
      LongQueue queue = new LongQueue(array, 50, 20);
      
      for(int i = 0; i < 10000; i++) {
         queue.offer(i+1);
      }
      for(int i = 10000-20; i < 10000; i++) {
         assertEquals(queue.poll(), i+1);
      }
   }
   
   public void testTailFollowsHead() throws Exception {
      long[] array = new long[10];
      LongQueue queue = new LongQueue(array, 5, 5);
      
      for(int i = 0; i < 1000; i++) {
         queue.offer(i+1);
         assertEquals(queue.size(), 1);
         assertEquals(queue.poll(), i+1);
         assertEquals(queue.size(), 0);
      }
   }
   
   public void testQueueOverflow() throws Exception {
      long[] array = new long[10];
      LongQueue queue = new LongQueue(array, 5, 5);
      
      for(int i = 0; i < 10; i++) {
         queue.offer(i+1);
      }
      assertEquals(queue.size(), 5);//1
      assertEquals(queue.poll(), 6);
      
      assertEquals(queue.size(), 4);//2
      assertEquals(queue.poll(), 7);
      
      assertEquals(queue.size(), 3);//3
      assertEquals(queue.poll(), 8);
      
      assertEquals(queue.size(), 2);//4
      assertEquals(queue.poll(), 9);
      
      assertEquals(queue.size(), 1);//5
      assertEquals(queue.poll(), 10);
   
      assertEquals(queue.size(), 0);
      assertEquals(queue.poll(), 0);
      assertEquals(queue.size(), 0);
      assertEquals(queue.poll(), 0);
      assertEquals(queue.size(), 0);
      assertEquals(queue.poll(), 0); 
   }
   
   public void testSmallQueue() throws Exception {
      long[] array = new long[5];
      LongQueue queue = new LongQueue(array, 3, 2);
      
      for(int i = 0; i < 2; i++) {
         queue.offer(i+1);
      }
      assertEquals(queue.size(), 2);
      assertEquals(queue.poll(), 1);
      assertEquals(queue.size(), 1);
      assertEquals(queue.poll(), 2);
      assertEquals(queue.size(), 0);
      assertEquals(queue.poll(), 0);
      assertEquals(queue.size(), 0);
      assertEquals(queue.poll(), 0); 
   }
   
   public void testTinyQueue() throws Exception {
      long[] array = new long[5];
      LongQueue queue = new LongQueue(array, 3, 1);
      
      for(int i = 0; i < 10000; i++) {
         queue.offer(i+1);
         assertEquals(queue.size(), 1);
         assertEquals(queue.poll(), i+1);
         assertEquals(queue.size(), 0);
      }
   }
}
