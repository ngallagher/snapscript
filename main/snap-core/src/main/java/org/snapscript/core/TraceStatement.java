package org.snapscript.core;

public class TraceStatement extends Statement {
   
   private final TraceAnalyzer analyzer;
   private final Statement statement;
   private final Trace trace;
   
   public TraceStatement(TraceAnalyzer analyzer, Statement statement, Trace trace) {
      this.statement = statement;
      this.analyzer = analyzer;
      this.trace = trace;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      return statement.compile(scope);
   }
   
   @Bug("must catch and throw InternalError with stack trace")
   @Override
   public Result execute(Scope scope) throws Exception {
      analyzer.before(scope, trace);
      
      try {
         return statement.execute(scope); 
      } finally {
         analyzer.after(scope, trace);
      }
   }
}
