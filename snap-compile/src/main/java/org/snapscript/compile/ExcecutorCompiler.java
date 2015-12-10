package org.snapscript.compile;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Model;

public class ExcecutorCompiler implements Compiler {
   
   private final Cache<String, Executable> cache;
   private final Executor executor;
   private final Compiler compiler;   
  
   public ExcecutorCompiler(Executor executor, Compiler compiler) {      
      this.cache = new LeastRecentlyUsedCache<String, Executable>();
      this.executor = executor;
      this.compiler = compiler;
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
         SourceCompilation compilation = new SourceCompilation(source, verbose);
         FutureTask<Executable> task = new FutureTask<Executable>(compilation);
         FutureExecutable result = new FutureExecutable(task);
         
         cache.cache(source, result);
         executor.execute(task);         
         
         return result;
      }
      return executable;
   }
   
   private class FutureExecutable implements Executable {
      
      private final FutureTask<Executable> result;
      private final Model model;
      
      public FutureExecutable(FutureTask<Executable> result) {
         this.model = new EmptyModel();
         this.result = result;
      }

      @Override
      public void execute() throws Exception {
         execute(model);
      }
      
      @Override
      public void execute(Model model) throws Exception {
         Executable executable = result.get();
         
         if(executable == null) {
            throw new IllegalStateException("Could not compile script");
         }
         executable.execute(model);
      }      
   }
   
   private class SourceCompilation implements Callable<Executable> {      
      
      private final String source;     
      private final boolean verbose;
      
      public SourceCompilation(String source) {
         this(source, false);
      }      
      
      public SourceCompilation(String source, boolean verbose) {
         this.verbose = verbose;
         this.source = source;
      }

      @Override
      public Executable call() {
         try {
            return compiler.compile(source, verbose);
         } catch(Exception cause) {
            return new ExceptionExecutable("Could not compile script", cause);
         }
      }            
   }
   
   private class ExceptionExecutable implements Executable {
      
      private final Exception cause;
      private final String message;
      
      public ExceptionExecutable(String message, Exception cause) {
         this.message = message;
         this.cause = cause;
      }      

      @Override
      public void execute() throws Exception {
         throw new IllegalStateException(message, cause);
      } 
      
      @Override
      public void execute(Model model) throws Exception {
         throw new IllegalStateException(message, cause);
      }             
   }  
}
