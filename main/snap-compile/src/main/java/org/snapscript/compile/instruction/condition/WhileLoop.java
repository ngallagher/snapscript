package org.snapscript.compile.instruction.condition;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;
import org.snapscript.core.Value;

public class WhileLoop implements Compilation {
   
   private final Statement loop;
   
   public WhileLoop(Evaluation evaluation, Statement body) {
      this.loop = new WhileStatement(evaluation, body);
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(analyzer, loop, trace);
   }
   
   private static class WhileStatement extends Statement {
   
      private final Evaluation condition;
      private final Statement body;
      
      public WhileStatement(Evaluation evaluation, Statement body) {
         this.condition = evaluation;
         this.body = body;
      }
      
      @Override
      public Result execute(Scope scope) throws Exception {
         while(true) {
            Value result = condition.evaluate(scope, null);
            Boolean value = result.getBoolean();
            
            if(value.booleanValue()) {
               Result next = body.execute(scope);
               
               if(next.isReturn()) {
                  return next;
               }
               if(next.isBreak()) {
                  return ResultType.getNormal();
               }
            } else {
               return ResultType.getNormal();
            } 
         }
      }
   }
}