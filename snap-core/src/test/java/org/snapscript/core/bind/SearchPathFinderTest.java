package org.snapscript.core.bind;

import java.util.List;

import junit.framework.TestCase;

import org.snapscript.core.Type;
import org.snapscript.core.index.ClassType;

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
      TypePathBuilder finder = new TypePathBuilder();
      Type a = new ClassType("A", "", null);
      Type b = new ClassType("B", "", null);
      Type c = new ClassType("C", "", null);
      Type d = new ClassType("D", "", null);
      Type x = new ClassType("X", "", null);
      Type y = new ClassType("Y", "", null);
      Type z = new ClassType("Z", "", null);
      Type i = new ClassType("I", "", null);
      Type j = new ClassType("J", "", null);
      Type k = new ClassType("K", "", null);
      
      List<Type> typesA = a.getTypes();
      List<Type> typesB = b.getTypes();
      List<Type> typesC = c.getTypes();
      List<Type> typesD = d.getTypes();
      
      typesB.add(a); // class B extends A with X,Y,Z
      typesB.add(x);
      typesB.add(y);
      typesB.add(z);
      
      typesC.add(b); // class C extends B
      
      typesD.add(c); // class D extends C with I,J,K
      typesD.add(i);
      typesD.add(j);
      typesD.add(k);
      
      List<Type> types = finder.createPath(d, "x");
      
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
