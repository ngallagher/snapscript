package org.snapscript.core;

public class TraceStatement extends Statement {
   
   private final TraceInterceptor interceptor;
   private final Statement statement;
   private final Trace trace;
   
   public TraceStatement(TraceInterceptor interceptor, Statement statement, Trace trace) {
      this.interceptor = interceptor;
      this.statement = statement;
      this.trace = trace;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      return statement.compile(scope);
   }
   
   @Bug("must catch and throw InternalError with stack trace")
   @Override
   public Result execute(Scope scope) throws Exception {
      try {
         interceptor.before(scope, trace);
         return statement.execute(scope); 
      } finally {
         interceptor.after(scope, trace);
      }
   }
}
