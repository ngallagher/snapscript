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
import org.snapscript.parse.StringToken;

public class Return implements Compilation {
   
   private final Statement control;
   
   public Return(StringToken token){
      this(null, token);
   }
   
   public Return(Evaluation evaluation){
      this(evaluation, null);
   }
   
   public Return(Evaluation evaluation, StringToken token){
      this.control = new ReturnStatement(evaluation);
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(analyzer, control, trace);
   }
   
   private static class ReturnStatement extends Statement {
   
      private final Evaluation evaluation;

      public ReturnStatement(Evaluation evaluation){
         this.evaluation = evaluation;
      }
      
      @Override
      public Result execute(Scope scope) throws Exception {
         if(evaluation != null) {
            Value value = evaluation.evaluate(scope, null);
            Object object = value.getValue();
            
            return ResultType.getReturn(object);
         }
         return ResultType.getReturn();
      }
   }
}