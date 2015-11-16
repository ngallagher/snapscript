package org.snapscript.interpret.console;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.common.io.ClassPathReader;
import org.snapscript.common.io.ResourceReader;
import org.snapscript.compile.ClassPathContext;
import org.snapscript.compile.Evaluator;
import org.snapscript.compile.StringEvaluator;
import org.snapscript.compile.assemble.InstructionLinker;
import org.snapscript.core.Context;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Function;
import org.snapscript.core.ImportResolver;
import org.snapscript.core.LibraryLinker;
import org.snapscript.core.Model;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class TypeTest extends TestCase {
   private static final Class[] TYPES = { LinkedList.class, HashMap.class, LinkedHashMap.class, String.class, Map.class};

   public void testTypes() throws Exception {
      Model model = new EmptyModel();
      Context context =new ClassPathContext(model);
      Evaluator evaluator = new StringEvaluator(context);
      LibraryLinker linker = new InstructionLinker(context);
      ResourceReader reader = new ClassPathReader(FunctionBinderTest.class);
      ImportResolver resolver = new ImportResolver(linker, reader);
      TypeLoader loader = new TypeLoader(resolver);

      for (Class name : TYPES) {
         long start = System.currentTimeMillis();
         try {
            Type type = loader.loadType(name);
            List<Function> functions = type.getFunctions();

            for (Function function : functions) {
               System.err.println(function);
            }
         } finally {
            long finish = System.currentTimeMillis();
            long duration = finish - start;

            System.err.println("Took " + duration + " for " + name);
            System.err.println();
         }
      }
   }
}
