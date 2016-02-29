package org.snapscript.core;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class ExecutorLinker implements PackageLinker {
   
   private final PackageLinker linker;
   private final Executor executor;
   
   public ExecutorLinker(PackageLinker linker, Executor executor) {
      this.executor = executor;
      this.linker = linker;
   }

   @Override
   public Package link(String resource, String source) throws Exception {
      PackageCompilation compilation = new PackageCompilation(resource, source);
      FutureTask<Package> task = new FutureTask<Package>(compilation);
      FuturePackage result = new FuturePackage(task, resource);
      
      executor.execute(task); 
      return result;
   }

   @Override
   public Package link(String resource, String source, String grammar) throws Exception {
      PackageCompilation compilation = new PackageCompilation(resource, source, grammar);
      FutureTask<Package> task = new FutureTask<Package>(compilation);
      FuturePackage result = new FuturePackage(task, resource);
      
      executor.execute(task); 
      return result;
   }
   
   private class FuturePackage implements Package {
      
      private final FutureTask<Package> result;
      private final String resource;
      
      public FuturePackage(FutureTask<Package> result, String resource) {
         this.resource = resource;
         this.result = result;
      }
      
      @Override
      public Statement compile(Scope scope) throws Exception {
         Package library = result.get();
         
         if(library == null) {
            throw new InternalStateException("Could not link " + resource);
         }
         return library.compile(scope);
      }      
   }
   
   private class PackageCompilation implements Callable<Package> {      
      
      private final String resource;
      private final String grammar;
      private final String source;     
      
      public PackageCompilation(String resource, String source) {
         this(resource, source, null);
      }
      
      public PackageCompilation(String resource, String source, String grammar) {
         this.resource = resource;
         this.grammar = grammar;
         this.source = source;
      }

      @Override
      public Package call() {
         try {
            if(grammar != null) {
               return linker.link(resource, source, grammar);
            }
            return linker.link(resource, source);
         } catch(Exception cause) {
            return new ExceptionPackage("Could not link " + resource, cause);
         }
      }            
   }
   
   private class ExceptionPackage implements Package {
      
      private final Exception cause;
      private final String message;
      
      public ExceptionPackage(String message, Exception cause) {
         this.message = message;
         this.cause = cause;
      }  
      
      @Override
      public Statement compile(Scope scope) throws Exception {
         throw new InternalStateException(message, cause);
      }             
   }

}
