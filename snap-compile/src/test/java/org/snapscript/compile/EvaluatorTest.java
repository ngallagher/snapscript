package org.snapscript.compile;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.core.Context;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.store.ClassPathStore;
import org.snapscript.core.store.Store;

public class EvaluatorTest extends TestCase {

   public void testEvaluator() throws Exception {
      Map<String, Object> map = new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      Store store = new ClassPathStore();
      Context context = new StoreContext(store);
      Evaluator evaluator = new StringEvaluator(context);
      Object result = evaluator.evaluate("1+2", model);
      assertEquals(result, 3);
   }

   public void testCompilerWithArgument() throws Exception {
      Map<String, Object> map = new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      Store store = new ClassPathStore();
      Context context = new StoreContext(store);
      Evaluator evaluator = new StringEvaluator(context);

      map.put("x", "blah");
      Object result = evaluator.evaluate("x.substring(1)", model);
      assertEquals(result, "lah");
   }

   public void testSignedLiteral() throws Exception {
      Map<String, Object> map = new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      Store store = new ClassPathStore();
      Context context = new StoreContext(store);
      Evaluator evaluator = new StringEvaluator(context);
      Object result = evaluator.evaluate("-1", model);
      assertEquals(result, -1);
   }

   public void testSignedLiteralCalc() throws Exception {
      Map<String, Object> map = new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      map.put("m", 10);
      Store store = new ClassPathStore();
      Context context = new StoreContext(store);
      Evaluator evaluator = new StringEvaluator(context);
      Object result = evaluator.evaluate("m * -1", model);
      assertEquals(result, -10);
   }

}
