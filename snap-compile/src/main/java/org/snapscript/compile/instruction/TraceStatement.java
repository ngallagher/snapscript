package org.snapscript.compile.instruction;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.parse.Line;

public class TraceStatement extends Statement {
   
   private final TraceAnalyzer analyzer;
   private final Statement statement;
   private final Line line;
   
   public TraceStatement(TraceAnalyzer analyzer, Statement statement, Line line) {
      this.analyzer = analyzer;
      this.statement = statement;
      this.line = line;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      return statement.compile(scope);
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      int number = line.getNumber();
      
      try {
         analyzer.before(scope, statement, number);
         return statement.execute(scope); 
      } finally {
         analyzer.after(scope, statement, number);
      }
   }
}
