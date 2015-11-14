package org.snapscript.core;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.core.bind.FunctionBinder;

public class FunctionBinderTest extends TestCase {
   
   public void testBinder() throws Exception {
      Map<String, Object> map = new HashMap<String, Object>();
      LibraryLinker linker = new LibraryLinker() {
         
         @Override
         public Library link(String name, String source) throws Exception {
            return null;
         }
         @Override
         public Library link(String name, String source, String grammar) throws Exception {
            return null;
         }
      };
      ImportStore store = new ImportStore();
      ImportResolver resolver = new ImportResolver(store, linker);
      TypeLoader loader = new TypeLoader(store, resolver);
      FunctionBinder binder = new FunctionBinder(loader);
      Type type = loader.load(Map.class);
      
      binder.bind(null, map, "put", "x", 11).call();
      binder.bind(null, map, "put", "y", 21).call();
      binder.bind(null, map, "put", "z", 441).call();
      
      assertEquals(map.get("x"), 11);
      assertEquals(map.get("y"), 21);
      assertEquals(map.get("z"), 441);
      
      binder.bind(null, map, "put", "x", 22).call();
      binder.bind(null, map, "remove", "y").call();
      binder.bind(null, map, "put", "z", "x").call();
      
      assertEquals(map.get("x"), 22);
      assertEquals(map.get("y"), null);
      assertEquals(map.get("z"), "x");
      
      assertEquals(binder.bind(null, map, "put", "x", 44).call().getValue(), 22);
      assertEquals(binder.bind(null, map, "put", "y", true).call().getValue(), null);
      assertEquals(binder.bind(null, map, "put", "z", "x").call().getValue(), "x");
   }

}
