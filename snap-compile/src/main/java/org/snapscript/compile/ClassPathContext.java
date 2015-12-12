package org.snapscript.compile;

import org.snapscript.core.Context;
import org.snapscript.core.ImportResolver;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.resource.ClassPathReader;
import org.snapscript.core.resource.ResourceReader;

public class ClassPathContext implements Context {

   private final ImportResolver resolver;
   private final TraceAnalyzer analyzer;
   private final FunctionBinder binder;
   private final ModuleBuilder builder;
   private final PackageLinker linker;
   private final ResourceReader reader;
   private final TypeLoader loader; 

   public ClassPathContext(){
      this.analyzer = new TraceAnalyzer();
      this.reader = new ClassPathReader();
      this.builder = new ModuleBuilder(this);
      this.linker = new ContextLinker(this);
      this.resolver = new ImportResolver(linker, reader);      
      this.loader = new TypeLoader(resolver);
      this.binder = new FunctionBinder(loader);
   }
   
   @Override
   public TraceAnalyzer getAnalyzer() {
      return analyzer;
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
