package org.snapscript.compile;

import org.snapscript.compile.Compiler;
import org.snapscript.compile.Executable;

import junit.framework.TestCase;

public class ExistentialOperatorTest extends TestCase {
   
   private static final String SOURCE=
   "var map={a:12,b:{c:33,d:null,e:{f:66}}};\n"+
   "println(map.a);\n"+
   "println(map.b.c);\n"+         
   "println(map?.b?.c);\n"+
   "println(map?.b?.d?.miss.blah);\n"+   
   "//println(map.b.d.miss.blah);\n"+     
   "println(map?.a);\n";     

   public void testNavigation() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE);
      executable.execute();
   }
}
