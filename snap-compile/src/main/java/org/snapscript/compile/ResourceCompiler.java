package org.snapscript.compile;

import org.snapscript.common.io.ResourceReader;
import org.snapscript.core.Compiler;
import org.snapscript.core.Context;
import org.snapscript.core.Executable;

public class ResourceCompiler implements Compiler {
   
   private final Compiler compiler;
   private final Context context;   
   
   public ResourceCompiler(Context context) {
      this.compiler = new StringCompiler(context);
      this.context = context;
   } 
   
   @Override
   public Executable compile(String resource) throws Exception {
      return compile(resource, false);
   }
   
   @Override
   public Executable compile(String resource, boolean verbose) throws Exception {
      ResourceReader reader = context.getReader();
      String source = reader.read(resource);
      
      return compiler.compile(source, verbose);
   } 
}
