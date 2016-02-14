package org.snapscript.compile.instruction;

import java.util.concurrent.atomic.AtomicInteger;

import org.snapscript.core.Statement;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.error.ErrorGenerator;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.parse.Line;

public class TraceWrapper {
   
   private final AtomicInteger counter;
   private final ErrorHandler handler;
   
   public TraceWrapper() {
      this.handler = new ErrorGenerator();
      this.counter = new AtomicInteger();
   }
   
   public Object wrap(TraceAnalyzer analyzer, Object value, Line line) {
      int key = counter.getAndIncrement();
      
      if(Statement.class.isInstance(value)) {
         Statement statement = (Statement)value;
         return new TraceStatement(analyzer, handler, statement, line, key);
      }
      if(Evaluation.class.isInstance(value)) {
         Evaluation evaluation = (Evaluation)value;
         return new TraceEvaluation(analyzer, evaluation, line, key);
      }
      return value;
   }
}
