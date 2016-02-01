package org.snapscript.compile.instruction;

import org.snapscript.core.Scope;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.Value;
import org.snapscript.parse.Line;

public class TraceEvaluation implements Evaluation {

   private final TraceAnalyzer analyzer;
   private final Evaluation evaluation;
   private final Trace trace;
   
   public TraceEvaluation(TraceAnalyzer analyzer, Evaluation evaluation, Line line, int key) {
      this.trace = new LineTrace(evaluation, line, key);
      this.evaluation = evaluation;
      this.analyzer = analyzer;
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
