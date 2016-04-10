package org.snapscript.compile.instruction;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;
import org.snapscript.core.Value;
import org.snapscript.core.error.ErrorHandler;

public class SwitchStatement implements Compilation {
   
   private final Statement statement;
   
   public SwitchStatement(Evaluation evaluation, Case... cases) {
      this.statement = new CompileResult(evaluation, cases);
   }
   
   @Override
   public Statement compile(Module module, int line) throws Exception {
      Context context = module.getContext();
      ErrorHandler handler = context.getHandler();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = TraceType.getNormal(module, line);
      
      return new TraceStatement(interceptor, handler, statement, trace);
   }
   
   private static class CompileResult extends Statement {

      private final Evaluation condition;
      private final Case[] cases;
      
      public CompileResult(Evaluation condition, Case... cases) {
         this.condition = condition;
         this.cases = cases;
      }
      
      @Override
      public Result compile(Scope scope) throws Exception {
         Result last = ResultType.getNormal();
         
         for(int i = 0; i < cases.length; i++){
            Statement statement = cases[i].getStatement();
            Result result = statement.compile(scope);
            
            if(!result.isNormal()){
               return result;
            }
            last = result;
         }
         return last;
      }
      
      @Override
      public Result execute(Scope scope) throws Exception {
         Value value = condition.evaluate(scope, null);
         Object left = value.getValue();
         
         for(int i = 0; i < cases.length; i++){
            Evaluation evaluation = cases[i].getEvaluation();
            
            if(evaluation == null) {
               Statement statement = cases[i].getStatement();
               Result result = statement.execute(scope);
               
               if(result.isBreak()) {
                  return ResultType.getNormal();
               }
               if(!result.isNormal()) {
                  return result;      
               }
               return ResultType.getNormal();
            }
            Value other = evaluation.evaluate(scope, null);
            Object right = other.getValue();
   
            if(left.equals(right)) {
               for(int j = i; j < cases.length; j++) {
                  Statement statement = cases[j].getStatement();
                  Result result = statement.execute(scope);
   
                  if(result.isBreak()) {
                     return ResultType.getNormal();
                  }
                  if(!result.isNormal()) {
                     return result;      
                  }
               }   
            }  
         }
         return ResultType.getNormal();
      }
   }
}
