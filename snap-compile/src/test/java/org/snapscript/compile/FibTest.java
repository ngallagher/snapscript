package org.snapscript.compile;

import junit.framework.TestCase;

public class FibTest extends TestCase {

   private static final String SOURCE=
   "function fib(n) {\n"+
   "   if (n<2) {\n"+
   "      return 1;\n"+
   "   }\n"+
   "   return fib(n-1) + fib(n-2);\n"+
   "}\n"+
   "var result = fib(30);\n"+
   "System.err.println(result);\n";
         
   public void testFib() throws Exception{
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE);
      long start = System.currentTimeMillis();
      
       executable.execute();
       System.err.println("time="+(System.currentTimeMillis()-start));
   }
   
   public static void main(String[] list) throws Exception {
      new FibTest().testFib();
   }
}
