package org.snapscript.interpret;

public class CalculatePI {

   public static void main(String[] list) throws Exception{
      calculate();
   }

   public static void calculate() {
      double pi = 4;
      boolean plus = false;
      for (int i = 3; i < 100000000; i += 2)
      {
          if (plus)
          {
              pi += 4.0 / i;
          }
          else
          {
              pi -= 4.0 / i;
          }
          plus = !plus;
       }
      System.err.println(pi);
   }
}
