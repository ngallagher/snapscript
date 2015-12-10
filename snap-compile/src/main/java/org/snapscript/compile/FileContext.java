package org.snapscript.compile;

import java.io.File;

import org.snapscript.core.Context;
import org.snapscript.core.ImportResolver;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.resource.FileReader;
import org.snapscript.core.resource.ResourceReader;

public class FileContext implements Context {

   private final ImportResolver resolver;
   private final FunctionBinder binder;
   private final ModuleBuilder builder;
   private final PackageLinker linker;
   private final ResourceReader reader;
   private final TypeLoader loader; 

   public FileContext(File file){
      this.reader = new FileReader(file);
      this.builder = new ModuleBuilder(this);
      this.linker = new ContextLinker(this);
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
   public PackageLinker getLinker() {
      return linker;
   }
}
