package org.snapscript.compile;

import java.util.Arrays;

import junit.framework.TestCase;

import org.snapscript.core.ClosureFunctionFinder;
import org.snapscript.core.Context;
import org.snapscript.core.EmptyFunction;
import org.snapscript.core.Function;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Parameter;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.convert.ConstraintConverter;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.convert.Score;
import org.snapscript.core.store.ClassPathStore;
import org.snapscript.core.store.Store;

public class ClosureMatcherTest extends TestCase {

   public void testClosureMatcher() throws Exception {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store);
      ConstraintMatcher matcher = context.getMatcher();
      TypeLoader loader = context.getLoader();
      ClosureFunctionFinder finder = new ClosureFunctionFinder(loader);
      Signature signature = new Signature(Arrays.asList(new Parameter("n", loader.loadType(String.class))));
      Type type = new EmptyFunction(signature).getDefinition();
      ConstraintConverter converter = matcher.match(type);
      Function function = new InvocationFunction(signature, null, type, null, "xx");
      Score score = converter.score(function);
      
      System.err.println(score);
   }
}
