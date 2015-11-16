package org.snapscript.assemble;

import org.snapscript.common.io.ClassPathReader;
import org.snapscript.common.io.ResourceReader;
import org.snapscript.core.Context;
import org.snapscript.core.ImportResolver;
import org.snapscript.core.LibraryLinker;
import org.snapscript.core.Model;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.bind.FunctionBinder;

public class ClassPathContext implements Context {

   private final FunctionBinder binder;
   private final ImportResolver resolver;
   private final ResourceReader reader;
   private final ModuleBuilder builder;
   private final LibraryLinker linker;
   private final TypeLoader loader; 

   public ClassPathContext(InstructionResolver res, Model model){
      this.reader = new ClassPathReader(ClassPathContext.class);
      this.builder = new ModuleBuilder(this, model);
      this.linker = new ScriptLinker(res, this);
      this.resolver = new ImportResolver(linker, reader);      
      this.loader = new TypeLoader(resolver);
      this.binder = new FunctionBinder(loader);
   }

   @Override
   public ModuleBuilder getBuilder() {
      return builder;
   }   

   @Override
   public FunctionBinder getBinder() {
      return binder;
   }

   @Override
   public TypeLoader getLoader() {
      return loader;
   }

   @Override
   public LibraryLinker getLinker() {
      return linker;
   } 
}
