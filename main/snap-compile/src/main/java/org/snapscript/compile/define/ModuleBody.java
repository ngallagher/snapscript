package org.snapscript.compile.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class ModuleBody extends Statement {

   private final Statement[] statements;
   private final AtomicBoolean execute;
   private final AtomicBoolean compile;
   private final AtomicBoolean define;
   
   public ModuleBody(Statement... statements) {
      this.execute = new AtomicBoolean(true);
      this.compile = new AtomicBoolean(true);
      this.define = new AtomicBoolean(true);
      this.statements = statements;
   }
   
   @Override
   public Result define(Scope scope) throws Exception {
      Result last = ResultType.getNormal();
      
      if(define.compareAndSet(true, false)) {
         for(Statement statement : statements) {
            Result result = statement.define(scope);
            
            if(!result.isNormal()){
               return result;
            }
            last = result;
         }
      }
      return last;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Result last = ResultType.getNormal();
      
      if(compile.compareAndSet(true, false)) {
         for(Statement statement : statements) {
            Result result = statement.compile(scope);
            
            if(!result.isNormal()){
               return result;
            }
            last = result;
         }
      }
      return last;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Result last = ResultType.getNormal();
      
      if(execute.compareAndSet(true, false)) {
         for(Statement statement : statements) {
            Result result = statement.execute(scope);
            
            if(!result.isNormal()){
               return result;
            }
            last = result;
         }
      }
      return last;
   }
}
