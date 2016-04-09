package org.snapscript.compile;

import java.util.Arrays;

import junit.framework.TestCase;

import org.snapscript.core.ClosureFunctionFinder;
import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.store.ClassPathStore;
import org.snapscript.core.store.Store;

public class ClosureFunctionFinderTest extends TestCase {

   public void testFunctionFinder() throws Exception {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store);
      TypeLoader loader = context.getLoader();
      ClosureFunctionFinder finder = new ClosureFunctionFinder(loader);
      Signature signature = new Signature(Arrays.asList("n"), Arrays.asList(loader.loadType(String.class)));
      Type type = new InvocationFunction(signature, null, null, null, "xx").getDefinition();
      Function function = finder.find(type);
      
      assertNotNull(function);
   }
}
