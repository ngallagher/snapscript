package org.snapscript.compile.link;

import static org.snapscript.compile.Instruction.SCRIPT_PACKAGE;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.compile.Instruction;
import org.snapscript.compile.function.PackageBuilder;
import org.snapscript.core.Context;
import org.snapscript.core.link.Package;
import org.snapscript.core.link.PackageLinker;

public class InstructionLinker implements PackageLinker {
   
   private final Cache<String, Package> cache;
   private final Instruction instruction;
   private final PackageBuilder builder;  
   
   public InstructionLinker(Context context) {
      this(context, SCRIPT_PACKAGE);
   }
   
   public InstructionLinker(Context context, Instruction instruction) {
      this.cache = new LeastRecentlyUsedCache<String, Package>();
      this.builder = new PackageBuilder(context);
      this.instruction = instruction;
   }
   
   @Override
   public Package link(String resource, String source) throws Exception {
      return link(resource, source, instruction.name);
   }
   
   @Override
   public Package link(String resource, String source, String grammar) throws Exception {
      Package linked = cache.fetch(resource);
      
      if(linked == null) {
         linked = builder.create(resource, source, grammar); 
         cache.cache(resource, linked);
      }
      return linked; 
   } 
}
