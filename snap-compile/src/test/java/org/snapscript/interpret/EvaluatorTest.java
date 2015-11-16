package org.snapscript.interpret;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.compile.Evaluator;
import org.snapscript.compile.StringEvaluator;
import org.snapscript.compile.context.ClassPathContext;
import org.snapscript.core.Context;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;

public class EvaluatorTest extends TestCase{
   
   public void testEvaluator() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      Context cc =new ClassPathContext(model);
      Evaluator evaluator = new StringEvaluator(cc);
      Object result = evaluator.evaluate("1+2");
      assertEquals(result, 3);
   }
   
   public void testCompilerWithArgument() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      Context cc =new ClassPathContext(model);
      Evaluator evaluator = new StringEvaluator(cc);

      map.put("x", "blah");
      Object result = evaluator.evaluate("x.substring(1)");
      assertEquals(result, "lah");
   }
   
   public void testSignedLiteral() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      Context cc =new ClassPathContext(model);
      Evaluator evaluator = new StringEvaluator(cc);
      Object result = evaluator.evaluate("-1");
      assertEquals(result, -1);
   }
   
   public void testSignedLiteralCalc() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      map.put("m", 10);
      Context cc =new ClassPathContext(model);
      Evaluator evaluator = new StringEvaluator(cc);
      Object result = evaluator.evaluate("m * -1");
      assertEquals(result, -10);
   }
   
}
