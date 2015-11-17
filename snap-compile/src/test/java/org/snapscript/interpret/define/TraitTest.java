package org.snapscript.interpret.define;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.compile.ClassPathContext;
import org.snapscript.compile.StringCompiler;
import org.snapscript.core.Context;
import org.snapscript.core.ContextModule;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.Value;
import org.snapscript.core.resource.ClassPathReader;
import org.snapscript.core.resource.ResourceReader;

public class TraitTest extends TestCase{
   private static final String SOURCE_1=
   "trait Pet {\n"+
   "   function speak() {\n"+
   "      out.println('Yo!');\n"+
   "   }\n"+
   "   function comeToMaster();\n"+
   "}\n"+
   "\n"+
   "class Dog extends Pet {\n"+
   "  // don't need to implement 'speak' if you don't want to\n"+
   "   function comeToMaster() {\n"+
   "      out.println('I\\'m coming!');\n"+
   "   }\n"+
   "}\n"+
   "\n"+
   "class Cat extends Pet {\n"+
   "  function speak() {\n"+
   "      out.println('meow');\n"+
   "  }\n"+
   "  function comeToMaster() {\n"+
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
      Context context =new ClassPathContext(s);
      ContextModule m = new ContextModule(context,s);
      StringCompiler compiler = new StringCompiler(context);
      boolean failure=false;
      System.err.println(SOURCE_1);
      compiler.compile(SOURCE_1).execute();
      System.err.println();

   }
}
