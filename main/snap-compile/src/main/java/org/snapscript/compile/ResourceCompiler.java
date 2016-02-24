package org.snapscript.compile;

import static org.snapscript.compile.instruction.Instruction.SCRIPT;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.compile.instruction.Instruction;
import org.snapscript.core.Context;
import org.snapscript.core.Package;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.PathConverter;
import org.snapscript.core.ResourceManager;

public class ResourceCompiler implements Compiler {

   private final Cache<String, Executable> cache;
   private final PathConverter converter;
   private final Instruction instruction;
   private final Context context;   
   
   public ResourceCompiler(Context context) {
      this(context, SCRIPT);
   }
   
   public ResourceCompiler(Context context, Instruction instruction) {
      this.cache = new LeastRecentlyUsedCache<String, Executable>();
      this.converter = new PathConverter();
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
         ResourceManager manager = context.getManager();
         String source = manager.getString(resource);
         PackageLinker linker = context.getLinker();
         Package library = linker.link(resource, source, instruction.name);
         String module = converter.createModule(resource);
         
         return new ContextExecutable(context, library, module);
      }
      return executable;
   } 
}
