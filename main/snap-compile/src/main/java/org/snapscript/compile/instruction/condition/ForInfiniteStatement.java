package org.snapscript.compile.instruction.condition;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;
import org.snapscript.core.error.ErrorHandler;

public class ForInfiniteStatement implements Compilation {
   
   private final Statement loop;
   
   public ForInfiniteStatement(Statement statement) {
      this.loop = new CompileResult(statement);
   }
   
   @Override
   public  Statement compile(Context context, String resource, int line) throws Exception {
      ErrorHandler handler = context.getHandler();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(interceptor, handler, loop, trace);
   }
   
   private static class CompileResult extends Statement {
      
      private final Statement body;
      
      public CompileResult(Statement body) {
         this.body = body;
      }
   
      @Override
      public Result execute(Scope scope) throws Exception {
         Scope compound = scope.getInner();
         
         while(true) {
            Result result = body.execute(compound);
            
            if(result.isReturn()) {
               return result;
            }
            if(result.isBreak()) {
               return ResultType.getNormal();
            }
         }
      }
   }
}
