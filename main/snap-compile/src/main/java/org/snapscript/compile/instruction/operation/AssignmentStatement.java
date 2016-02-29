package org.snapscript.compile.instruction.operation;

import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;
import org.snapscript.core.Value;
import org.snapscript.core.error.ErrorHandler;

public class AssignmentStatement implements Compilation {
   
   private final Statement assignment;
   
   public AssignmentStatement(Evaluation identifier, Evaluation value) {
      this.assignment = new CompileResult(identifier, value);
   }
   
   @Override
   public Statement compile(Module module, int line) throws Exception {
      Context context = module.getContext();
      ErrorHandler handler = context.getHandler();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = TraceType.getNormal(module, line);
      
      return new TraceStatement(interceptor, handler, assignment, trace);
   }

   private static class CompileResult extends Statement {
      
      private final NameExtractor extractor;
      private final Evaluation value;
      
      public CompileResult(Evaluation identifier, Evaluation value) {
         this.extractor = new NameExtractor(identifier);
         this.value = value;
      }
      
      @Override
      public Result execute(Scope scope) throws Exception {
         Value result = value.evaluate(scope, null);
         String name = extractor.extract(scope);
         State state = scope.getState();
         
         state.setValue(name, result);
         
         return ResultType.getNormal();
      }
   }
}