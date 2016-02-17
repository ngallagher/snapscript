package org.snapscript.core;

public class TraceEvaluation implements Evaluation {

   private final TraceInterceptor interceptor;
   private final Evaluation evaluation;
   private final Trace trace;
   
   public TraceEvaluation(TraceInterceptor interceptor, Evaluation evaluation, Trace trace) {
      this.interceptor = interceptor;
      this.evaluation = evaluation;
      this.trace = trace;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      try {
         interceptor.before(scope, trace);
         return evaluation.evaluate(scope, left); 
      } finally {
         interceptor.after(scope, trace);
      }
   }
}
