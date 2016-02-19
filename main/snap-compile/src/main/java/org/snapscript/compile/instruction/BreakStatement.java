package org.snapscript.compile.instruction;

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
import org.snapscript.parse.StringToken;

public class BreakStatement implements Compilation {
   
   private final Statement control;
   
   public BreakStatement(StringToken token) {
      this.control = new Delegate();
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      Trace trace = TraceType.getNormal(resource, line);
      TraceInterceptor interceptor = context.getInterceptor();
      ErrorHandler handler = context.getHandler();
      
      return new TraceStatement(interceptor, handler, control, trace);
   }
   
   private static class Delegate extends Statement {
      
      private final Result result;
      
      public Delegate(){
         this.result = ResultType.getBreak();
      }
      
      @Override
      public Result execute(Scope scope) throws Exception {
         return result;
      }
   }
}