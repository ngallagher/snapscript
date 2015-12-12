package org.snapscript.compile.instruction;

import org.snapscript.core.Scope;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.Value;
import org.snapscript.parse.Line;

public class TraceEvaluation implements Evaluation {

   private final TraceAnalyzer analyzer;
   private final Evaluation evaluation;
   private final Line line;
   
   public TraceEvaluation(TraceAnalyzer analyzer, Evaluation evaluation, Line line) {
      this.analyzer = analyzer;
      this.evaluation = evaluation;
      this.line = line;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      int number = line.getNumber();
      
      try {
         analyzer.before(scope, evaluation, number);
         return evaluation.evaluate(scope, left); 
      } finally {
         analyzer.after(scope, evaluation, number);
      }
   }

}
