package org.snapscript.compile.instruction;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.parse.Line;

public class TraceStatement extends Statement {
   
   private final TraceAnalyzer analyzer;
   private final ErrorHandler handler;
   private final Statement statement;
   private final Trace trace;
   
   public TraceStatement(TraceAnalyzer analyzer, ErrorHandler handler, Statement statement, Line line, int key) {
      this.trace = new LineTrace(statement, line, key);
      this.statement = statement;
      this.analyzer = analyzer;
      this.handler = handler;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      return statement.compile(scope);
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      analyzer.before(scope, trace);
      
      try {
         Result result = statement.execute(scope);
         
         if(result.isThrow()) {
            return handler.throwError(scope, trace, result);
         }
         return result;
      } catch(Throwable cause) {
         return handler.throwError(scope, trace, cause);
      } finally {
         analyzer.after(scope, trace);
      }
   }
}
