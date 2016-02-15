package org.snapscript.compile.instruction;

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

public class Throw implements Compilation {
   
   private final Statement control;
   
   public Throw(Evaluation evaluation) {
      this.control = new ThrowStatement(evaluation);
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getConstruct(resource, line);
      
      return new TraceStatement(analyzer, control, trace);
   }
   
   private static class ThrowStatement extends Statement {
   
      private final Evaluation evaluation;
      
      public ThrowStatement(Evaluation evaluation) {
         this.evaluation = evaluation; 
      }   
   
      @Override
      public Result execute(Scope scope) throws Exception {
         Value reference = evaluation.evaluate(scope, null);
         Object value = reference.getValue();
         
         return ResultType.getThrow(value);  
      }
   }

}
