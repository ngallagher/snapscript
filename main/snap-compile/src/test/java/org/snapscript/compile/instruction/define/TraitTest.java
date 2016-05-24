package org.snapscript.compile.instruction.define;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.compile.ClassPathCompilerBuilder;
import org.snapscript.compile.Compiler;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.Value;

public class TraitTest extends TestCase{
   private static final String SOURCE_1=
   "trait Pet {\n"+
   "   speak() {\n"+
   "      out.println('Yo!');\n"+
   "   }\n"+
   "   comeToMaster();\n"+
   "}\n"+
   "\n"+
   "class Dog extends Pet {\n"+
   "  // don't need to implement 'speak' if you don't want to\n"+
   "   comeToMaster() {\n"+
   "      out.println('I\\'m coming!');\n"+
   "   }\n"+
   "}\n"+
   "\n"+
   "class Cat extends Pet {\n"+
   "  speak() {\n"+
   "      out.println('meow');\n"+
   "  }\n"+
   "  comeToMaster() {\n"+
   "      out.println('That\\'s not gonna happen.');\n"+
   "   }\n"+
   "}\n"+
   "var d=new Dog();\n"+
   "d.speak();\n"+
   "d.comeToMaster();\n"+
   "var c =new Cat();\n"+
   "c.speak();\n"+
   "c.comeToMaster();\n";

   public static void main(String[] l)throws Exception{
      new TraitTest().testTraits();
   }
   public void testTraits() throws Exception {
      Map map = new HashMap<String,Value>();
      map.put("out",System.out);
      Model s = new MapModel(map);
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      boolean failure=false;
      System.err.println(SOURCE_1);
      compiler.compile(SOURCE_1).execute(s);
      System.err.println();

   }
}
