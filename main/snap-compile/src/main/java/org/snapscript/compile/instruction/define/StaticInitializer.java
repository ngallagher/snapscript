package org.snapscript.compile.instruction.define;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public abstract class StaticInitializer extends Initializer {

   private final AtomicReference<Type> argument;
   private final FutureTask<Result> future;
   private final AtomicLong reference;
   private final CompileTask task;
   
   protected StaticInitializer() {
      this.argument = new AtomicReference<Type>();
      this.task = new CompileTask();
      this.future = new FutureTask<Result>(task);
      this.reference = new AtomicLong(-1);
   }

   @Override
   public Result compile(Scope scope, Type type) throws Exception { 
      Thread thread = Thread.currentThread();
      long key = thread.getId();
      
      if(argument.compareAndSet(null, type)) {
         reference.set(key); // remember thread
         future.run();
      } 
      if(reference.compareAndSet(key, key)) { // short circuit recursion
         return ResultType.getNormal();
      }
      return future.get(); // deadlock is possible
   }
   
   protected abstract void compile(Type type) throws Exception; 
   
   private class CompileTask implements Callable<Result> {
      
      private final Result result;
      
      public CompileTask(){
         this.result = ResultType.getNormal();
      }

      @Override
      public Result call() throws Exception {
         Type type = argument.get();
         
         if(type != null) {
            compile(type);
         }
         return result;
      } 
   }
}
