package org.snapscript.compile.instruction;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;

public class TryFinally implements Compilation {
   
   private final Statement statement;
   
   public TryFinally(Statement body, Statement finish) {
      this.statement = new TryFinallyStatement(body, finish);
   }    
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(analyzer, statement, trace);
   }
   
   private static class TryFinallyStatement extends Statement {
   
      private final Statement statement;
      private final Statement finish;
      
      public TryFinallyStatement(Statement statement, Statement finish) {
         this.statement = statement;
         this.finish = finish;
      }    
   
      @Override
      public Result execute(Scope scope) throws Exception {
         try {
            return statement.execute(scope);
         } finally {
            finish.execute(scope);         
         }
      }
   }
}
