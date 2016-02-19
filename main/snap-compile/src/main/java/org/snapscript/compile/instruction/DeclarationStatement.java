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

public class DeclarationStatement implements Compilation {
   
   private final Statement declaration;   
   
   public DeclarationStatement(Evaluation declaration) {
      this.declaration = new CompileResult(declaration);     
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      ErrorHandler handler = context.getHandler();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(interceptor, handler, declaration, trace);
   }
   
   private static class CompileResult extends Statement {

      private final Evaluation declaration;   
      
      public CompileResult(Evaluation declaration) {
         this.declaration = declaration;     
      }
      
      @Override
      public Result execute(Scope scope) throws Exception {
         Value variable = declaration.evaluate(scope, null);              
         Object value = variable.getValue();      
         
         return ResultType.getNormal(value);
      }
   }
}