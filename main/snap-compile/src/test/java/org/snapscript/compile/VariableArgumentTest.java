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
   
   private static final String SOURCE_2 =
   "function fun(a: Long, b...: Double) {\n"+
   "   println(a);\n"+
   "   for(var i in b) {\n"+
   "      println('b='+i);\n"+
   "   }\n"+
   "   println(b.size());\n"+
   "}\n"+
   "\n"+
   "fun(1,22,33,44,55);\n";
   
   private static final String SOURCE_3 =
   "function fun(a: Long, b...: Boolean) {\n"+
   "   println(a);\n"+
   "   for(var i in b) {\n"+
   "      println('b='+i);\n"+
   "   }\n"+
   "   println(b.size());\n"+
   "}\n"+
   "\n"+
   "fun(1,true,33,44,55);\n";
         
         
   public void testVariableArguments() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_1);
      executable.execute();
   }

   public void testVariableArgumentsWithConstraint() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_2);
      executable.execute();
   }
   
   public void testVariableArgumentsWithFailure() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_3);
      boolean failure = false;
      try {
         executable.execute();
      } catch(Throwable e) {
         failure = true;
         e.printStackTrace();
      }
      assertTrue(failure);
   }
   
   public static void main(String[] list) throws Exception {
      new VariableArgumentTest().testVariableArguments();
      new VariableArgumentTest().testVariableArgumentsWithConstraint();
      new VariableArgumentTest().testVariableArgumentsWithFailure();
   }
}
