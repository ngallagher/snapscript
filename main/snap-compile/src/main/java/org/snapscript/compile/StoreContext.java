package org.snapscript.compile;

import org.snapscript.core.Context;
import org.snapscript.core.ExpressionEvaluator;
import org.snapscript.core.ModuleRegistry;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.ResourceManager;
import org.snapscript.core.StoreManager;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.store.Store;

public class StoreContext implements Context {

   private final ExpressionEvaluator executor;
   private final ConstraintMatcher matcher;
   private final ResourceManager manager;
   private final TraceAnalyzer analyzer;
   private final FunctionBinder binder;
   private final ModuleRegistry registry;
   private final PackageLinker linker;
   private final TypeLoader loader; 

   public StoreContext(Store store){
      this.analyzer = new TraceAnalyzer();
      this.manager = new StoreManager(store);
      this.registry = new ModuleRegistry(this);
      this.linker = new ContextLinker(this);      
      this.loader = new TypeLoader(linker, registry, manager);
      this.matcher = new ConstraintMatcher(loader);
      this.binder = new FunctionBinder(matcher, loader);
      this.executor = new ContextEvaluator(this);
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
   public ExpressionEvaluator getEvaluator() {
      return executor;
   }

   @Override
   public ModuleRegistry getRegistry() {
      return registry;
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
