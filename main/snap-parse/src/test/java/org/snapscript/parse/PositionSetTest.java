package org.snapscript.parse;

import java.lang.management.ManagementFactory;
import java.security.SecureRandom;
import java.util.Random;

import org.snapscript.common.LeastRecentlyUsedSet;

import com.sun.management.ThreadMXBean;

import junit.framework.TestCase;

public class PositionSetTest extends TestCase {
   
   private static final int ITERATIONS = 10000;
   
   public void testPerformance() throws Exception {
      ThreadMXBean bean = (ThreadMXBean)ManagementFactory.getThreadMXBean();
      Random random = new SecureRandom();
      Long[] values = new Long[ITERATIONS];
      
      for(int i = 0; i < values.length; i++) {
         values[i] = random.nextLong();
      }
      long id = Thread.currentThread().getId();
      for(int n = 0; n  < 10; n++) {
         PositionSet cache = new PositionSet(100);
         LeastRecentlyUsedSet<Long> set = new LeastRecentlyUsedSet<Long>(100);
         System.gc();
         Thread.sleep(1000);
         long memoryStart1 = bean.getThreadAllocatedBytes(id);
         long start1 = System.currentTimeMillis();
         
         for(int i = 0; i < values.length; i++) {
            cache.add(values[i]);
         }
         long finish1 = System.currentTimeMillis();
         long memoryFinish1 = bean.getThreadAllocatedBytes(id);
         System.gc();
         Thread.sleep(1000);
         long memoryStart2 = bean.getThreadAllocatedBytes(id);
         long start2 = System.currentTimeMillis();
         
         for(int i = 0; i < values.length; i++) {
            set.add(values[i]);
         }
         long finish2 = System.currentTimeMillis();
         long memoryFinish2 = bean.getThreadAllocatedBytes(id);
         System.err.println("PositionSet          time="+(finish1-start1) + " memory="+(memoryFinish1 - memoryStart1));
         System.err.println("LeastRecentlyUsedSet time="+(finish2-start2)+ " memory="+(memoryFinish2 - memoryStart2));
      }
   }
   
   public void testLargeCache() throws Exception {
      PositionSet cache = new PositionSet(10);
      LeastRecentlyUsedSet<Long> set = new LeastRecentlyUsedSet<Long>(10);
      Random random = new SecureRandom();
      
      for(int i = 0; i < 10000; i++) {
         Long value = random.nextLong();
         cache.add(value);
         set.add(value);
      }
      assertEquals(set.size(), 10);
      assertEquals(cache.size(), 10);
      
      for(Long value : set) {
         assertTrue(cache.contains(value));
      }
   }

   public void testSimpleCache() throws Exception {
      PositionSet cache = new PositionSet(10);
      
      assertTrue(cache.add(11L));
      assertTrue(cache.add(12L));
      assertTrue(cache.add(13L));
      assertTrue(cache.add(14L));
      assertTrue(cache.add(15L));
      assertEquals(cache.size(), 5);
   }
}
