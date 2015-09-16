package org.snapscript.core.execute;

import org.snapscript.Compiler;
import org.snapscript.Executable;
import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Context;

public class ScriptCompiler implements Compiler {
   
   private final Cache<String, Executable> cache;
   private final Context context;   
   
   public ScriptCompiler(Context context) {
      this.cache = new LeastRecentlyUsedCache<String, Executable>();
      this.context = context;
   } 
   
   @Override
   public Executable compile(String source) throws Exception {
      return compile(source, false);
   }
   
   @Override
   public Executable compile(String source, boolean verbose) throws Exception {
      if(source == null) {
         return new NoScript();
      }
      Executable executable = cache.fetch(source);
      
      if(executable == null) {
         LibraryLinker linker = context.getLinker();
         Library library = linker.link(source);
         
         return new ExecutableLibrary(context, library);
      }
      return executable;
   } 
}
