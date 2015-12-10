package org.snapscript.interpret;

import org.snapscript.compile.ClassPathContext;
import org.snapscript.compile.Compiler;
import org.snapscript.compile.StringCompiler;
import org.snapscript.core.Context;

public class ClassPathCompilerBuilder {

   public static Compiler createCompiler() {
      Context context =new ClassPathContext();
      return new StringCompiler(context);
   }
}
