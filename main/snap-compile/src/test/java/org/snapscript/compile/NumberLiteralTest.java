package org.snapscript.compile;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

import junit.framework.TestCase;

import com.sun.management.ThreadMXBean;

public class NumberLiteralTest  extends TestCase {

   private static final String SOURCE=
   "println(0b01);\n"+
   "println(0xff);\n";

   public void testLiterals() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE);
      executable.execute();
   }

   public static void main(String[] list) throws Exception {
      new NumberLiteralTest().testLiterals();
   }
}
