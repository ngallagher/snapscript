package org.snapscript.compile;

import org.snapscript.core.Context;
import org.snapscript.core.resource.ClassPathStore;
import org.snapscript.core.resource.Store;

public class ClassPathCompilerBuilder {

   public static Compiler createCompiler() {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store);
      
      return new StringCompiler(context);
   }
}
