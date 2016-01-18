package org.snapscript.compile;

import junit.framework.TestCase;

import org.snapscript.core.EmptyModel;

public class ModuleTest extends TestCase {
   
   private static final String SOURCE =
   "class X{\n"+
   "}\n"+
   "var y = X.class.getModule();\n"+
   "System.err.println(y);\n";

   public void testModule() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      compiler.compile(SOURCE).execute(new EmptyModel());
   }
}
