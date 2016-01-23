package org.snapscript.compile;

import junit.framework.TestCase;

import org.snapscript.core.EmptyModel;

public class ModuleTest extends TestCase {
   
   private static final String SOURCE =
   "module M{\n"+
   "   class X{\n"+
   "      var i;\n"+
   "      new(i){\n"+
   "         this.i = i;\n"+
   "      }\n"+
   "      toString(){\n"+
   "         return \"\"+i;\n"+
   "      }\n"+
   "   }\n"+
   "   var x = new X(11);\n"+
   "   var y = x.class.getModule();\n"+
   "\n"+
   "   System.err.println(this);\n"+
   "   System.err.println(x);\n"+
   "}\n"+
   "var x = new M.X(33);\n"+
   "System.err.println(x);\n"+
   "\n";


   public void testModule() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      System.err.println(SOURCE);
      compiler.compile(SOURCE).execute(new EmptyModel());
   }
}
