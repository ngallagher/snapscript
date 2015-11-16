package org.snapscript.compile;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Context;
import org.snapscript.core.Executable;
import org.snapscript.core.ExecutableLibrary;
import org.snapscript.core.Library;
import org.snapscript.core.LibraryLinker;

public class StringCompiler implements Compiler {
   
   private final Cache<String, Executable> cache;
   private final Context context;   
   
   public StringCompiler(Context context) {
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
         throw new NullPointerException("No source provided");
      }
      Executable executable = cache.fetch(source);
      
      if(executable == null) {
         LibraryLinker linker = context.getLinker();
         Library library = linker.link(null, source, "script");
         
         return new ExecutableLibrary(context, library);
      }
      return executable;
   } 
}
