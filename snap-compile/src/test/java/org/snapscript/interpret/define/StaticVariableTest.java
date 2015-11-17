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

public class StaticVariableTest extends TestCase{
   public static class Pet{
      public static final String DOG="Dog";
      public static final String CAT="Cat";
      public static final String FISH="Fish";
   }
   private static final String SOURCE_1=
   "import com.zuooh.script.build.define.StaticVariableTest$Pet;\n"+
   "\n"+
   "out.println('dog='+StaticVariableTest$Pet.DOG);\n"+
   "out.println('cat='+StaticVariableTest$Pet.CAT);\n"+
   "out.println('fish='+StaticVariableTest$Pet.FISH);\n";

   private static final String SOURCE_2=
   "class Pet{\n"+
   "   static var DOG='Dog';\n"+
   "   static var CAT='Cat';\n"+
   "   static var FISH='Fish';\n"+
   "}\n"+
   "\n"+
   "out.println('Pet.DOG='+Pet.DOG);\n"+
   "out.println('Pet.CAT='+Pet.CAT);\n"+
   "out.println('Pet.FISH='+Pet.FISH);\n";
   
   public static void main(String[] l)throws Exception{
      new StaticVariableTest().testEnums();
   }
   public void testEnums() throws Exception {
      Map map = new HashMap<String,Value>();
      map.put("out",System.out);
      Model s = new MapModel(map);
      Context context =new ClassPathContext(s);
      ContextModule m = new ContextModule(context,s);
      StringCompiler compiler = new StringCompiler(context);
      boolean failure=false;
      System.err.println(SOURCE_1);
      //compiler.compile(SOURCE_1).execute(s);
      System.err.println();
      System.err.println(SOURCE_2);
      compiler.compile(SOURCE_2).execute();
      System.err.println();      

   }
}