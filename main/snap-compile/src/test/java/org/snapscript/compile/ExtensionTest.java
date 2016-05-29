package org.snapscript.compile;

import junit.framework.TestCase;

public class ExtensionTest extends TestCase {

   private static final String SOURCE = 
   "var list = new File(\".\").find(\".*\");\n"+
   "println(list);\n";
   
   public void testExtension() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      System.err.println(SOURCE);
      Executable executable = compiler.compile(SOURCE);
      executable.execute();
   }
     
}
