package org.snapscript.interpret;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Compiler;
import org.snapscript.core.Executable;
import org.snapscript.core.Model;
import org.snapscript.core.NoScript;

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
         return new NoScript();
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
      
      public FutureExecutable(FutureTask<Executable> result) {
         this.result = result;
      }

      @Override
      public void execute() throws Exception {
         Executable executable = result.get();
         
         if(executable == null) {
            throw new IllegalStateException("Could not compile script");
         }
         executable.execute();
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
   }  
}
