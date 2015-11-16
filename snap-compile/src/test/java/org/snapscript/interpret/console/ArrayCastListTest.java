package org.snapscript.interpret.console;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class ArrayCastListTest extends TestCase {

   public void testCast() throws Exception {
      byte[] b = new byte[100];
      byte[][] bb = new byte[100][];
      Object[] o = (Object[])bb;
      List l = convert(b);
      
      System.err.println(l.size());
   }
   
   private static List convert(Object array) {
      Class type = array.getClass();
      
      if(type == byte[].class) {
         return Arrays.asList((byte[])array);
      }
      if(type == int[].class) {
         return Arrays.asList((int[])array);
      }
      if(type == long[].class) {
         return Arrays.asList((long[])array);
      }
      if(type == double[].class) {
         return Arrays.asList((double[])array);
      }
      if(type == float[].class) {
         return Arrays.asList((float[])array);
      }
      if(type == short[].class) {
         return Arrays.asList((short[])array);
      }
      if(type == char[].class) {
         return Arrays.asList((char[])array);
      }
      return Arrays.asList((Object[])array);
   }
}
