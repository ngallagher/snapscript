package org.snapscript.compile.instruction.condition;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;

public class InfiniteLoop implements Compilation {
   
   private final Statement loop;
   
   public InfiniteLoop(Statement statement) {
      this.loop = new ForStatement(statement);
   }
   
   @Override
   public  Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(analyzer, loop, trace);
   }
   
   private static class ForStatement extends Statement {
      
      private final Statement body;
      
      public ForStatement(Statement body) {
         this.body = body;
      }
   
      @Override
      public Result execute(Scope scope) throws Exception {
         Scope compound = scope.getInner();
         
         while(true) {
            Result result = body.execute(compound);
            
            if(result.isReturn() || result.isThrow()) {
               return result;
            }
            if(result.isBreak()) {
               return ResultType.getNormal();
            }
         }
      }
   }
}
