package org.snapscript.interpret.console;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import junit.framework.TestCase;

import org.snapscript.compile.ContextLinker;
import org.snapscript.compile.ClassPathContext;
import org.snapscript.compile.Evaluator;
import org.snapscript.compile.StringEvaluator;
import org.snapscript.core.Context;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Function;
import org.snapscript.core.ImportResolver;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.Model;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.resource.ClassPathReader;
import org.snapscript.core.resource.ResourceReader;

public class FunctionBinderTest extends TestCase {

   public void testBinderMethod() throws Exception {
      Model model = new EmptyModel();
      ResourceReader reader = new ClassPathReader();
      Context context =new ClassPathContext();
      Evaluator evaluator = new StringEvaluator(context);
      PackageLinker linker = new ContextLinker(context);
      ImportResolver resolver = new ImportResolver(linker, reader);
      TypeLoader loader = new TypeLoader(resolver);
      FunctionBinder binder = new FunctionBinder(loader);
      
      Type type =loader.loadType(PrintStream.class);
      List<Function> functions = type.getFunctions();

      for (Function function : functions) {
         System.err.println(function);
      }
      Callable<Result> call = binder.bind(null, System.err, "printf", "some string %s=%s%n", 12.33d, 33);
      call.call();
   }
   

   public void testBinderConstruct() throws Exception {
      Model model = new EmptyModel();
      ResourceReader reader = new ClassPathReader();
      Context context =new ClassPathContext();
      Evaluator evaluator = new StringEvaluator(context);
      PackageLinker linker = new ContextLinker(context);
      ImportResolver resolver = new ImportResolver(linker, reader);
      TypeLoader loader = new TypeLoader(resolver);
      FunctionBinder binder = new FunctionBinder(loader);
      Map<String, Property>v=new LinkedHashMap<String,Property>();
      Type type =loader.loadType(String.class);
      List<Function> functions = type.getFunctions();

      for (Function function : functions) {
         System.err.println(function);
      }
      Callable<Result> call = binder.bind(null, type, "new", "some string");
      Result result = call.call();
      Object vs = result.getValue();
      List<Property> variables = type.getProperties();
      
      for(Property variable : variables){
         String name = variable.getName();
         System.err.println(name);
         v.put(name, variable);
      }
      assertEquals(vs, "some string");
     /* Variable value = v.get("value");
      Accessor accessor = value.getAccessor();
      char[] chs=accessor.getValue(result);
      assertEquals(chs.length, "some string".length());*/
   }
}
