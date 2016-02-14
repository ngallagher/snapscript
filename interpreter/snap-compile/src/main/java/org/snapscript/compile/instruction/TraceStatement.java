package org.snapscript.compile.instruction;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.exception.StackTraceUpdater;
import org.snapscript.parse.Line;

public class TraceStatement extends Statement {
   
   private final TraceAnalyzer analyzer;
   private final StackTraceUpdater appender;
   private final Statement statement;
   private final Trace trace;
   
   public TraceStatement(TraceAnalyzer analyzer, Statement statement, Line line, int key) {
      this.trace = new LineTrace(statement, line, key);
      this.appender = new StackTraceUpdater(trace);
      this.statement = statement;
      this.analyzer = analyzer;
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
            return appender.update(scope, result);
         }
         return result;
      } catch(Throwable cause) {
         return appender.update(scope, cause);
      } finally {
         analyzer.after(scope, trace);
      }
   }
}
