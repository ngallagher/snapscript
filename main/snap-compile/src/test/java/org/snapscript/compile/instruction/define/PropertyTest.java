package org.snapscript.compile.instruction.define;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.compile.ClassPathCompilerBuilder;
import org.snapscript.compile.Compiler;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.Value;

public class PropertyTest extends TestCase{
   public static class Pet{
      public static final String DOG="Dog";
      public static final String CAT="Cat";
      public static final String FISH="Fish";
      private int count;
      public Pet(){
         super();
      }
      public int getCount(){
         return count;
      }
      public void setCount(int count){
         this.count=count;
      }
   }
   private static final String SOURCE_1=
   "import "+Pet.class.getName()+";\n"+
   "\n"+
   "var pet =new PropertyTest$Pet();\n"+
   "pet.count++;\n"+
   "pet.count++;\n"+   
   "out.println('count='+pet.count);\n";

   private static final String SOURCE_2=
   "class Blah{\n"+
   "   var x = 2;\n"+
   "   getText(){\n"+
   "      return 'text';\n"+
   "   }\n"+
   "}\n"+
   "var b = new Blah();\n"+
   "println(b.text);\n";

//   public static void main(String[] l)throws Exception{
//      new PropertyTest().testProperties();
//   }
//   public void testProperties() throws Exception {
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
   
   public void testScopeProperties() throws Exception {
      Map map = new HashMap<String,Value>();
      map.put("out",System.out);
      Model s = new MapModel(map);
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      boolean failure=false;
      System.err.println(SOURCE_2);
      compiler.compile(SOURCE_2).execute(s);
      System.err.println();  

   }
}
