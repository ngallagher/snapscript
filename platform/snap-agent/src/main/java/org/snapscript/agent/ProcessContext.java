package org.snapscript.agent;

import java.net.URI;
import java.util.concurrent.Executor;

import org.snapscript.agent.debug.BreakpointMatcher;
import org.snapscript.agent.debug.SuspendController;
import org.snapscript.agent.profiler.ProcessProfiler;
import org.snapscript.common.ThreadPool;
import org.snapscript.compile.ResourceCompiler;
import org.snapscript.compile.StoreContext;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Model;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.TraceInterceptor;

public class ProcessContext {

   private final SuspendController controller;
   private final ResourceCompiler compiler;
   private final ProcessProfiler profiler;
   private final BreakpointMatcher matcher;
   private final StoreContext context;
   private final ProcessStore store;
   private final Executor executor;
   private final Model model;
   private final String process;

   public ProcessContext(URI root, String process, int port) {
      this(root, process, port, 4);
   }
   
   public ProcessContext(URI root, String process, int port, int threads) {
      this.store = new ProcessStore(root);
      this.executor = new ThreadPool(4);
      this.context = new StoreContext(store, executor);
      this.compiler = new ResourceCompiler(context);
      this.controller = new SuspendController();
      this.matcher = new BreakpointMatcher();
      this.profiler = new ProcessProfiler();
      this.model = new EmptyModel();
      this.process = process;
   }
   
   public PackageLinker getLinker() {
      return context.getLinker();
   }
   
   public TraceInterceptor getInterceptor() {
      return context.getInterceptor();
   }
   
   public ResourceCompiler getCompiler() {
      return compiler;
   }
   
   public ProcessProfiler getProfiler() {
      return profiler;
   }
   
   public BreakpointMatcher getMatcher() {
      return matcher;
   }
   
   public SuspendController getController() {
      return controller;
   }
   
   public ProcessStore getStore() {
      return store;
   }
   
   public Model getModel() {
      return model;
   }
   
   public String getProcess() {
      return process;
   }
}
