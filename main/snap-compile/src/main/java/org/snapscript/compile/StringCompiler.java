package org.snapscript.compile;

import static org.snapscript.compile.assemble.Instruction.SCRIPT;
import static org.snapscript.core.Reserved.DEFAULT_PACKAGE;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.compile.assemble.Program;
import org.snapscript.compile.assemble.Instruction;
import org.snapscript.core.Context;
import org.snapscript.core.PathConverter;
import org.snapscript.core.link.Package;
import org.snapscript.core.link.PackageLinker;

public class StringCompiler implements Compiler {
   
   private final Cache<String, Executable> cache;
   private final PathConverter converter;
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
      this.converter = new PathConverter();
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
         String path = converter.createPath(module);
         
         return new Program(context, library, module, path);
      }
      return executable;
   } 
}
