package org.snapscript.compile.instruction;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;
import org.snapscript.core.Value;
import org.snapscript.core.error.ErrorHandler;

public class ExpressionStatement implements Compilation {
   
   private final Statement expression;
   
   public ExpressionStatement(Evaluation expression) {
      this.expression = new Delegate(expression);
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      ErrorHandler handler = context.getHandler();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(interceptor, handler, expression, trace);
   }
   
   private static class Delegate extends Statement {
      
      private final Evaluation expression;
   
      public Delegate(Evaluation expression) {
         this.expression = expression;
      }
   
      @Override
      public Result execute(Scope scope) throws Exception {
         Value reference = expression.evaluate(scope, null);
         Object value = reference.getValue();
         
         return ResultType.getNormal(value);
      }
   }
}