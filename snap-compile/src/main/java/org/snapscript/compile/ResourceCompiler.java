package org.snapscript.compile;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.compile.instruction.Instruction;
import org.snapscript.core.Context;
import org.snapscript.core.Package;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.resource.ResourceReader;

public class ResourceCompiler implements Compiler {

   private final Cache<String, Executable> cache;
   private final Instruction instruction;
   private final Context context;   
   
   public ResourceCompiler(Context context) {
      this(context, Instruction.SCRIPT);
   }
   
   public ResourceCompiler(Context context, Instruction instruction) {
      this.cache = new LeastRecentlyUsedCache<String, Executable>();
      this.instruction = instruction;
      this.context = context;
   } 
   
   @Override
   public Executable compile(String resource) throws Exception {
      return compile(resource, false);
   }
   
   @Override
   public Executable compile(String resource, boolean verbose) throws Exception {
      if(resource == null) {
         throw new NullPointerException("No resource provided");
      }
      Executable executable = cache.fetch(resource);
      
      if(executable == null) {
         ResourceReader reader = context.getReader();
         String source = reader.read(resource);
         PackageLinker linker = context.getLinker();
         Package library = linker.link(resource, source, instruction.name);
         
         return new ContextExecutable(context, library);
      }
      return executable;
   } 
}
