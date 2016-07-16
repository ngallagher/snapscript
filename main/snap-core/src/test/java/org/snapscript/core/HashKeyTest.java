package org.snapscript.core;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class HashKeyTest extends TestCase {
   
   private static final int COUNT = 10;
   private static final int ITERATIONS = 10000000;
   
   public void testKeys() throws Exception {
      Integer[] intKeys = new Integer[COUNT];
      String[] stringKeys = new String[COUNT];
      Map<String, String> stringMap = new HashMap<String, String>();
      Map<Integer, Integer> intMap = new HashMap<Integer, Integer>();
      
      for(int i = 0; i < COUNT; i++) {
         intKeys[i] = i;
      }
      for(int i = 0; i < COUNT; i++) {
         stringKeys[i] = "string-" + i;
      }
      timeTest(() -> {
         for(int i = 0; i < COUNT; i++) {
            intMap.put(intKeys[i], intKeys[i]);
         }
      }, "Integer put");
      timeTest(() -> {
         for(int i = 0; i < COUNT; i++) {
            stringMap.put(stringKeys[i], stringKeys[i]);
         }
      }, "String put");
      timeTest(() -> {
         for(int i = 0; i < COUNT; i++) {
            intMap.get(intKeys[i]);
         }
      }, "Integer get");
      
      timeTest(() -> {
         for(int i = 0; i < COUNT; i++) {
            stringMap.get(stringKeys[i]);
         }
      }, "String get");
   }
   
   private void timeTest(Runnable task, String description) throws Exception {
      task.run(); // prime the hashCode
      long start = System.currentTimeMillis();
      for(int i = 0; i < ITERATIONS; i++) {
         task.run();
      }
      long finish = System.currentTimeMillis();
      System.err.println("Time took " + (finish-start) + " for " + description);
   }

}
