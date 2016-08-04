package org.snapscript.compile;

import static org.snapscript.compile.assemble.Instruction.SCRIPT;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.compile.Compiler;
import org.snapscript.compile.Executable;
import org.snapscript.compile.StoreContext;
import org.snapscript.compile.StringCompiler;
import org.snapscript.core.Context;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.ExpressionEvaluator;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.store.ClassPathStore;
import org.snapscript.core.store.Store;

public class DynamicConstructionTest extends TestCase {

   private static final String SOURCE =
   "class Point{\n"+
   "   const x;\n"+
   "   const y;\n"+
   "   new(x,y){\n"+
   "      this.x=x;\n"+
   "      this.y=y;\n"+
   "   }\n"+
   "   draw(){\n"+
   "      println(x+','+y);\n"+
   "   }\n"+
   "   toString(){\n"+
   "      return '('+x+','+y+')';\n"+
   "   }\n"+
   "}\n"+
   "\n"+
   "class Line {\n"+
   "   const a;\n"+
   "   const b;\n"+
   "   new(a: Point, b: Point){\n"+
   "      this.a=a;\n"+
   "      this.b=b;\n"+
   "   }\n"+
   "   draw(){\n"+
   "      println(a+'->'+b);\n"+
   "   }\n"+
   "   toString(){\n"+
   "      return a+'->'+b;\n"+
   "   }\n"+
   "}\n";
   
   public void testDynamicConstruction() throws Exception {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store, null);
      Compiler compiler = new StringCompiler(context, SCRIPT, "blah");
      System.err.println(SOURCE);
      Executable executable = compiler.compile(SOURCE);
      executable.execute();
      ExpressionEvaluator evaluator = context.getEvaluator();
      Model model = new EmptyModel();
      Object a = evaluator.evaluate(model, "new Point(1,2)", "blah");
      Object b = evaluator.evaluate(model, "new Point(10,20)", "blah");    
      Map map1 = new HashMap();
      map1.put("a", a);
      map1.put("b", b);
      Model model1 = new MapModel(map1);
      Object shape = evaluator.evaluate(model1, "new Line(a,b)", "blah"); 
      Map map2 = new HashMap();
      map2.put("shape", shape);
      Model model2 = new MapModel(map2);
      Object text = evaluator.evaluate(model2, "shape.toString()", "blah");
      System.err.println(text);
      assertEquals(text, "(1,2)->(10,20)");
   }

}
