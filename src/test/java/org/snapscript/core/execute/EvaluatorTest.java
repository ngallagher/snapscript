package org.snapscript.core.execute;

import java.util.LinkedHashMap;
import java.util.Map;

import org.snapscript.Evaluator;
import org.snapscript.Model;
import org.snapscript.core.Context;
import org.snapscript.core.MapModel;
import org.snapscript.core.ScriptContext;
import org.snapscript.core.execute.ExpressionEvaluator;

import junit.framework.TestCase;

public class EvaluatorTest extends TestCase{
   
   public void testEvaluator() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      Context cc=new ScriptContext();
      Evaluator evaluator = new ExpressionEvaluator(cc);
      Object result = evaluator.evaluate("1+2", model);
      assertEquals(result, 3);
   }
   
   public void testCompilerWithArgument() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      Context cc=new ScriptContext();
      Evaluator evaluator = new ExpressionEvaluator(cc);

      map.put("x", "blah");
      Object result = evaluator.evaluate("x.substring(1)", model);
      assertEquals(result, "lah");
   }
   
   public void testSignedLiteral() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      Context cc=new ScriptContext();
      Evaluator evaluator = new ExpressionEvaluator(cc);
      Object result = evaluator.evaluate("-1", model);
      assertEquals(result, -1);
   }
   
   public void testSignedLiteralCalc() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      Context cc=new ScriptContext();
      map.put("m", 10);
      Evaluator evaluator = new ExpressionEvaluator(cc);
      Object result = evaluator.evaluate("m * -1", model);
      assertEquals(result, -10);
   }
   
}
