package org.snapscript.core.define;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.Model;
import org.snapscript.core.Context;
import org.snapscript.core.MapModel;
import org.snapscript.core.ScriptContext;
import org.snapscript.core.Value;
import org.snapscript.core.execute.ScriptCompiler;

import junit.framework.TestCase;

public class EnumTest extends TestCase{
   private static final String SOURCE_1=
   "enum Animal {\n"+
   "   DOG,\n"+
   "   CAT,\n"+
   "   FISH;\n"+
   "}\n"+
   "out.println('enum for DOG='+Animal.DOG.name+' class='+Animal.DOG.class+' ordinal='+Animal.DOG.ordinal);\n"+
   "out.println('enum for CAT='+Animal.CAT.name+' class='+Animal.CAT.class+' ordinal='+Animal.CAT.ordinal);\n"+
   "out.println('enum for FISH='+Animal.FISH.name+' class='+Animal.FISH.class+' ordinal='+Animal.FISH.ordinal);\n";   

   private static final String SOURCE_2=
   "enum Person {\n"+
   "   JIM('Jim',23),\n"+
   "   TOM('Tom',33),\n"+
   "   BOB('Bob',21);\n"+
   "   var title;\n"+
   "   var age;\n"+
   "   new(title,age){\n"+
   "      this.title=title;\n"+
   "      this.age=age;\n"+
   "   }\n"+
   "}\n"+
   "out.println('enum for JIM='+Person.JIM.name+' class='+Person.JIM.class+' ordinal='+Person.JIM.ordinal+' age='+Person.JIM.age+' title='+Person.JIM.title);\n"+
   "out.println('enum for TOM='+Person.TOM.name+' class='+Person.TOM.class+' ordinal='+Person.TOM.ordinal+' age='+Person.TOM.age+' title='+Person.TOM.title);\n"+
   "out.println('enum for BOB='+Person.BOB.name+' class='+Person.BOB.class+' ordinal='+Person.BOB.ordinal+' age='+Person.BOB.age+' title='+Person.BOB.title);\n";   
   public static void main(String[] l)throws Exception{
      new EnumTest().testEnums();
   }
   public void testEnums() throws Exception {
      Map map = new HashMap<String,Value>();
      map.put("out",System.out);
      Model s = new MapModel(map);
      Context context = new ScriptContext();
      ScriptCompiler compiler = new ScriptCompiler(context);
      boolean failure=false;
      System.err.println(SOURCE_1);
      //compiler.compile(SOURCE_1).execute(s);
      System.err.println();
      System.err.println(SOURCE_2);
      compiler.compile(SOURCE_2).execute(s);
      System.err.println();      

   }
}
