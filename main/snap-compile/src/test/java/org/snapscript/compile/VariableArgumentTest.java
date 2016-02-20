package org.snapscript.compile;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

import com.sun.management.ThreadMXBean;

import junit.framework.TestCase;

public class VariableArgumentTest extends TestCase {
   
   private static final String SOURCE_1 =
   "function fun(a, b...) {\n"+
   "   println(a);\n"+
   "}\n"+
   "fun(1,2,3,4,5);\n";
         
   public void testVariableArguments() throws Exception {
      DecimalFormat format = new DecimalFormat("###,###,###,###,###");
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_1);
      executable.execute();
   }

   public static void main(String[] list) throws Exception {
      new VariableArgumentTest().testVariableArguments();
   }
}
