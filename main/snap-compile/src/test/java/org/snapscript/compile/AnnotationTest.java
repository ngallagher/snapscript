package org.snapscript.compile;

import junit.framework.TestCase;

public class AnnotationTest extends TestCase {

   private static final String SOURCE=
   "var map = {x: @Blah(a: '/path', b: 11)};\n"+
   "println(map);\n";

   public void testAnnotation() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE);
      executable.execute();
   }
}
