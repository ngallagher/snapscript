package org.snapscript.ide;

import java.net.URI;

import org.snapscript.compile.ContextLinker;
import org.snapscript.core.Context;
import org.snapscript.core.ImportResolver;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.resource.RemoteReader;
import org.snapscript.core.resource.ResourceReader;

public class ScriptAgentContext implements Context {

   private final ImportResolver resolver;
   private final TraceAnalyzer analyzer;
   private final FunctionBinder binder;
   private final ModuleBuilder builder;
   private final PackageLinker linker;
   private final ResourceReader reader;
   private final ConstraintMatcher matcher;
   private final TypeLoader loader; 

   public ScriptAgentContext(URI root){
      this.analyzer = new TraceAnalyzer();
      this.reader = new RemoteReader(root);
      this.builder = new ModuleBuilder(this);
      this.linker = new ContextLinker(this);
      this.resolver = new ImportResolver(linker, reader);      
      this.loader = new TypeLoader(resolver);
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
