package org.snapscript.core.execute;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.assembler.InstructionResolver;
import org.snapscript.assembler.ScriptContext;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluator;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;

public class EvaluatorTest extends TestCase{
   
   public void testEvaluator() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      InstructionResolver set = new InterpretationResolver();
      Context cc =new ScriptContext(set);
      Evaluator evaluator = new ExpressionEvaluator(set,cc);
      Object result = evaluator.evaluate("1+2", model);
      assertEquals(result, 3);
   }
   
   public void testCompilerWithArgument() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      InstructionResolver set = new InterpretationResolver();
      Context cc =new ScriptContext(set);
      Evaluator evaluator = new ExpressionEvaluator(set,cc);

      map.put("x", "blah");
      Object result = evaluator.evaluate("x.substring(1)", model);
      assertEquals(result, "lah");
   }
   
   public void testSignedLiteral() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      InstructionResolver set = new InterpretationResolver();
      Context cc =new ScriptContext(set);
      Evaluator evaluator = new ExpressionEvaluator(set,cc);
      Object result = evaluator.evaluate("-1", model);
      assertEquals(result, -1);
   }
   
   public void testSignedLiteralCalc() throws Exception{
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      map.put("m", 10);
      InstructionResolver set = new InterpretationResolver();
      Context cc =new ScriptContext(set);
      Evaluator evaluator = new ExpressionEvaluator(set,cc);
      Object result = evaluator.evaluate("m * -1", model);
      assertEquals(result, -10);
   }
   
}
