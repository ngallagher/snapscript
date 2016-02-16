package org.snapscript.compile.instruction.operation;

import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;
import org.snapscript.core.Value;

public class AssignmentStatement implements Compilation {
   
   private final Statement assignment;
   
   public AssignmentStatement(Evaluation identifier, Evaluation value) {
      this.assignment = new Delegate(identifier, value);
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(analyzer, assignment, trace);
   }

   private static class Delegate extends Statement {
      
      private final NameExtractor extractor;
      private final Evaluation value;
      
      public Delegate(Evaluation identifier, Evaluation value) {
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