package org.snapscript.compile;

import junit.framework.TestCase;

public class ReturnConstraintTest extends TestCase {

   private static final String SOURCE =
   "class Blah{\n"+
   "   get():Blah{\n"+
   "      return this;\n"+
   "   }\n"+
   "}\n"+
   "function func(x: Blah):Blah{\n"+
   "   return x.get();\n"+
   "}\n"+
   "var b = new Blah();\n"+
   "func(b);\n";   

   public void testMap() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE);
      executable.execute();
   }
}
