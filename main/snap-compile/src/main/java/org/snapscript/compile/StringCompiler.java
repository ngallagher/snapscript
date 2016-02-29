package org.snapscript.compile;

import static org.snapscript.compile.instruction.Instruction.SCRIPT;
import static org.snapscript.core.Reserved.DEFAULT_PACKAGE;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.compile.instruction.Application;
import org.snapscript.compile.instruction.Instruction;
import org.snapscript.core.Context;
import org.snapscript.core.Package;
import org.snapscript.core.PackageLinker;

public class StringCompiler implements Compiler {
   
   private final Cache<String, Executable> cache;
   private final Instruction instruction;
   private final Context context;   
   private final String module;
   
   public StringCompiler(Context context) {
      this(context, SCRIPT);
   }
   
   public StringCompiler(Context context, Instruction instruction) {
      this(context, instruction, DEFAULT_PACKAGE);
   }
   
   public StringCompiler(Context context, Instruction instruction, String module) {
      this.cache = new LeastRecentlyUsedCache<String, Executable>();
      this.instruction = instruction;
      this.context = context;
      this.module = module;
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
         PackageLinker linker = context.getLinker();
         Package library = linker.link(module, source, instruction.name);
         
         return new Application(context, library, module);
      }
      return executable;
   } 
}
