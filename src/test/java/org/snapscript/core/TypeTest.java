package org.snapscript.core;

import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.ScriptContext;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.TypeResolver;
import org.snapscript.core.execute.LibraryLinker;
import org.snapscript.core.execute.ScriptLinker;

import junit.framework.TestCase;

public class TypeTest extends TestCase {
   private static final String[] TYPES = { "java.util.LinkedList", "java.util.HashMap", "java.util.LinkedHashMap", "java.lang.String", "java.util.Map"};

   public void testTypes() throws Exception {
      Context context = new ScriptContext();
      LibraryLinker linker = new ScriptLinker(context);
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
