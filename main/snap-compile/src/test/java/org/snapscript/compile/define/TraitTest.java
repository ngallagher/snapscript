package org.snapscript.compile.define;

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
   
   private static final String SOURCE_2 =
   "trait X{\n"+
   "   const x = 11;\n"+
   "   dump(){\n"+
   "      println(x);\n"+
   "   }\n"+
   "}\n"+
   "class Y with X{\n"+
   "}\n"+
   "new Y().dump();\n";
   
   private static final String SOURCE_3 =
   "class X{\n"+
   "   static const x = 11;\n"+
   "   static dump(){\n"+
   "      println(x);\n"+
   "   }\n"+
   "}\n"+
   "X.x;\n";   
         
//   public void testTraits() throws Exception {
//      Map map = new HashMap<String,Value>();
//      map.put("out",System.out);
//      Model s = new MapModel(map);
//      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
//      boolean failure=false;
//      System.err.println(SOURCE_1);
//      compiler.compile(SOURCE_1).execute(s);
//      System.err.println();
//
//   }
   
   public void testTraitConstants() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      boolean failure=false;
      System.err.println(SOURCE_2);
      compiler.compile(SOURCE_2).execute();
      System.err.println();

   }
   
//   public void testClassConstants() throws Exception {
//      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
//      boolean failure=false;
//      System.err.println(SOURCE_3);
//      compiler.compile(SOURCE_3).execute();
//      System.err.println();
//
//   }   
}
