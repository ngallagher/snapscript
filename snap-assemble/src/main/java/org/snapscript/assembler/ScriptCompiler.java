package org.snapscript.assembler;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Compiler;
import org.snapscript.core.Context;
import org.snapscript.core.Executable;
import org.snapscript.core.ExecutableLibrary;
import org.snapscript.core.Library;
import org.snapscript.core.LibraryLinker;
import org.snapscript.core.NoScript;

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
