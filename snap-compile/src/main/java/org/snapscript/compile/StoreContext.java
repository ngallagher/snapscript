package org.snapscript.compile;

import org.snapscript.core.Context;
import org.snapscript.core.ImportResolver;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.ResourceManager;
import org.snapscript.core.StoreManager;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.store.Store;

public class StoreContext implements Context {

   private final ConstraintMatcher matcher;
   private final ResourceManager manager;
   private final ImportResolver resolver;
   private final TraceAnalyzer analyzer;
   private final FunctionBinder binder;
   private final ModuleBuilder builder;
   private final PackageLinker linker;
   private final TypeLoader loader; 

   public StoreContext(Store store){
      this.analyzer = new TraceAnalyzer();
      this.manager = new StoreManager(store);
      this.builder = new ModuleBuilder(this);
      this.linker = new ContextLinker(this);
      this.resolver = new ImportResolver(linker, manager);      
      this.loader = new TypeLoader(resolver, builder);
      this.matcher = new ConstraintMatcher(loader);
      this.binder = new FunctionBinder(matcher, loader);
   }
   
   @Override
   public ConstraintMatcher getMatcher() {
      return matcher;
   }
   
   @Override
   public TraceAnalyzer getAnalyzer() {
      return analyzer;
   }
   
   @Override
   public ResourceManager getManager() {
      return manager;
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
