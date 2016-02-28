package org.snapscript.compile;

import junit.framework.TestCase;

public class ImportTest extends TestCase {

   private static final String SOURCE=
   "import awt.Color as RGB;\n"+
   "println(RGB.black);";

   public void testImport() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE);
      executable.execute();
   }

   public static void main(String[] list) throws Exception {
      new ImportTest().testImport();
   }
}
