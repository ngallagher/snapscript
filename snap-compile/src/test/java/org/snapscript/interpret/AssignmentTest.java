package org.snapscript.interpret;

import junit.framework.TestCase;

import org.snapscript.compile.Compiler;
import org.snapscript.compile.Executable;

public class AssignmentTest extends TestCase{
   
   private static final String SOURCE=
   "class Point{\n"+
   "   var x:Double;\n"+
   "   var y:Double;\n"+
   "   new(x,y){\n"+
   "      this.x=x;\n"+
   "      this.y=y;\n"+
   "   }\n"+
   "   dump(){\n"+
   "      System.err.println(\"${x},${y}\");\n"+
   "   }\n"+
   "}\n"+
   "var p = new Point(true,false);\n"+
   "p.dump();\n";
         
   public void testAssignmentConstraints() throws Exception{
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE);
      boolean failure = false;
      
      try {
         executable.execute();
      }catch(Exception e){
         failure=true;
         e.printStackTrace();
      }
      assertTrue(failure);
   }
}
