package org.snapscript.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.Property;
import org.snapscript.core.ScriptContext;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.TypeResolver;
import org.snapscript.core.convert.FunctionBinder;
import org.snapscript.core.execute.LibraryLinker;
import org.snapscript.core.execute.Result;
import org.snapscript.core.execute.ScriptLinker;

import junit.framework.TestCase;

public class FunctionBinderTest extends TestCase {

   public void testBinderMethod() throws Exception {
      Context context = new ScriptContext();
      LibraryLinker linker = new ScriptLinker(context);
      TypeResolver resolver = new TypeResolver(linker);
      TypeLoader loader = new TypeLoader(resolver);
      FunctionBinder binder = new FunctionBinder(loader);
      
      Type type =loader.load("java.io.PrintStream",null);
      List<Function> functions = type.getFunctions();

      for (Function function : functions) {
         System.err.println(function);
      }
      Callable<Result> call = binder.bind(null, System.err, "printf", "some string %s=%s%n", 12.33d, 33);
      call.call();
   }
   

   public void testBinderConstruct() throws Exception {
      Context context = new ScriptContext();
      LibraryLinker linker = new ScriptLinker(context);
      TypeResolver resolver = new TypeResolver(linker);
      TypeLoader loader = new TypeLoader(resolver);
      FunctionBinder binder = new FunctionBinder(loader);
      Map<String, Property>v=new LinkedHashMap<String,Property>();
      Type type =loader.load("java.lang.String",null);
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
