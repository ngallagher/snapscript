package org.snapscript.compile;

import java.io.File;

import org.snapscript.common.io.FileReader;
import org.snapscript.common.io.ResourceReader;
import org.snapscript.compile.assemble.InstructionLinker;
import org.snapscript.core.Context;
import org.snapscript.core.ImportResolver;
import org.snapscript.core.LibraryLinker;
import org.snapscript.core.Model;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.bind.FunctionBinder;

public class FileContext implements Context {

   private final ImportResolver resolver;
   private final FunctionBinder binder;
   private final ResourceReader reader;
   private final ModuleBuilder builder;
   private final LibraryLinker linker;
   private final TypeLoader loader; 

   public FileContext(Model model, File file){
      this.reader = new FileReader(file);
      this.builder = new ModuleBuilder(this, model);
      this.linker = new InstructionLinker(this);
      this.resolver = new ImportResolver(linker, reader);      
      this.loader = new TypeLoader(resolver);
      this.binder = new FunctionBinder(loader);
   }
   
   @Override
   public ResourceReader getReader() {
      return reader;
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
