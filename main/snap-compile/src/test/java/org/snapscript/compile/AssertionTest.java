package org.snapscript.compile;

import junit.framework.TestCase;

public class AssertionTest extends TestCase {

   private static final String SOURCE=
   "assert true;";

   public void testAssertions() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE);
      executable.execute();
   }
}
