package org.snapscript.core.bind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.snapscript.core.Type;

public class SearchPathFinderTest extends TestCase {
   
   /*
    * class A {
    * }
    * class B extends A with X,Y,Z{
    * }
    * class C extends B{
    * }
    * class D extends C with I,J,K{
    * }
    * trait I{
    * }
    * 
    * D,C,B,A,I,J,K,X,Y,Z,Runnable
    */
   public void testSearchPath() throws Exception {
      SearchPathFinder finder = new SearchPathFinder();
      List<Type> typesA = new ArrayList<Type>();
      List<Type> typesB = new ArrayList<Type>();
      List<Type> typesC = new ArrayList<Type>();
      List<Type> typesD = new ArrayList<Type>();
      Type a = new Type("A", null, typesA, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      Type b = new Type("B", null, typesB, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      Type c = new Type("C", null, typesC, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      Type d = new Type("D", null, typesD, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      Type x = new Type("X", null, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      Type y = new Type("Y", null, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      Type z = new Type("Z", null, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      Type i = new Type("I", null, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      Type j = new Type("J", null, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      Type k = new Type("K", null, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      
      typesB.add(a); // class B extends A with X,Y,Z
      typesB.add(x);
      typesB.add(y);
      typesB.add(z);
      
      typesC.add(b); // class C extends B
      
      typesD.add(c); // class D extends C with I,J,K
      typesD.add(i);
      typesD.add(j);
      typesD.add(k);
      
      List<Type> types = finder.createPath(d);
      
      System.err.println(types);
      
      assertEquals(d, types.get(0));
      assertEquals(c, types.get(1));
      assertEquals(b, types.get(2));
      assertEquals(a, types.get(3));
      assertEquals(i, types.get(4));
      assertEquals(j, types.get(5));
      assertEquals(k, types.get(6));
      assertEquals(x, types.get(7));
      assertEquals(y, types.get(8));
      assertEquals(z, types.get(9));
      
   }

}
