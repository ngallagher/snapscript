package org.snapscript.core;

import junit.framework.TestCase;

public class SuperTest extends TestCase {

   static interface A{
      public void a();
   }
   static interface B{
      public void b();
   }
   static class X implements A, B {
      public void a(){
         System.err.println("X.a()");
      }
      public void b(){
         System.err.println("X.b()");
         a();
      }
   }
   static class Y extends X implements A{
      public void c(){
         super.b();
      }
      public void a(){
         System.err.println("Y.a()");
      }
   }

   public void testSuper() throws Exception {
      Y y = new Y();
      y.c();
   }
}
