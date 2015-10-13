package org.snapscript.interpret.console;

import java.util.List;

import junit.framework.TestCase;

import org.snapscript.assemble.InstructionResolver;
import org.snapscript.assemble.ScriptContext;
import org.snapscript.assemble.ScriptLinker;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluator;
import org.snapscript.core.Function;
import org.snapscript.core.LibraryLinker;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.TypeResolver;
import org.snapscript.interpret.ExpressionEvaluator;
import org.snapscript.interpret.InterpretationResolver;

public class TypeTest extends TestCase {
   private static final String[] TYPES = { "java.util.LinkedList", "java.util.HashMap", "java.util.LinkedHashMap", "java.lang.String", "java.util.Map"};

   public void testTypes() throws Exception {
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      Evaluator evaluator = new ExpressionEvaluator(set,context);
      LibraryLinker linker = new ScriptLinker(set, context);
      TypeResolver resolver = new TypeResolver(linker);
      TypeLoader loader = new TypeLoader(resolver);

      for (String name : TYPES) {
         long start = System.currentTimeMillis();
         try {
            Type type = loader.load(name,null);
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