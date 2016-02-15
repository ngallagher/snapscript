package org.snapscript.core;

public class TraceEvaluation implements Evaluation {

   private final TraceAnalyzer analyzer;
   private final Evaluation evaluation;
   private final Trace trace;
   
   public TraceEvaluation(TraceAnalyzer analyzer, Evaluation evaluation, Trace trace) {
      this.evaluation = evaluation;
      this.analyzer = analyzer;
      this.trace = trace;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      try {
         analyzer.before(scope, trace);
         return evaluation.evaluate(scope, left); 
      } finally {
         analyzer.after(scope, trace);
      }
   }
}
