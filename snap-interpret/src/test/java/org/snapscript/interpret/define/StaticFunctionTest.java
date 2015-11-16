package org.snapscript.interpret.define;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.assemble.InstructionSet;
import org.snapscript.assemble.StringCompiler;
import org.snapscript.assemble.ClassPathContext;
import org.snapscript.core.Context;
import org.snapscript.core.ContextModule;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.Value;
import org.snapscript.interpret.OperationSet;

public class StaticFunctionTest extends TestCase{
   public static class Pet{
      public static final String DOG="Dog";
      public static final String CAT="Cat";
      public static final String FISH="Fish";
      public static void dump(String t){
         System.out.println("I am dumping!!=="+t);
      }
   }
   private static final String SOURCE_1=
   "import "+Pet.class.getName()+";\n"+
   "\n"+
   "StaticFunctionTest$Pet.dump('shit');\n";

   private static final String SOURCE_2=
   "class Pet{\n"+
   "   static var DOG='Dog';\n"+
   "   static var CAT='Cat';\n"+
   "   static var FISH='Fish';\n"+
   "   static dump(t){\n"+
   "      out.println('dump from Pet='+t);\n"+
   "   }\n"+
   "}\n"+
   "\n"+
   "Pet.dump('shit');\n";
   
   public static void main(String[] l)throws Exception{
      new StaticFunctionTest().testEnums();
   }
   public void testEnums() throws Exception {
      Map map = new HashMap<String,Value>();
      map.put("out",System.out);
      Model s = new MapModel(map);
      InstructionSet set = new OperationSet();
      Context context =new ClassPathContext(set, s);
      ContextModule m = new ContextModule(context, s);
      StringCompiler compiler = new StringCompiler(context);
      boolean failure=false;
      System.err.println(SOURCE_1);
      compiler.compile(SOURCE_1).execute();
      System.err.println();
      System.err.println(SOURCE_2);
      compiler.compile(SOURCE_2).execute();
      System.err.println();      

   }
}
