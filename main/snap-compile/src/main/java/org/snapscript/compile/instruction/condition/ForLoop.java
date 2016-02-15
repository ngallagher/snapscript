package org.snapscript.compile.instruction.condition;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;
import org.snapscript.core.Value;

public class ForLoop implements Compilation {
   
   private final Statement loop;
   
   public ForLoop(Statement declaration, Evaluation evaluation, Statement statement) {
      this(declaration, evaluation, null, statement);
   }
   
   public ForLoop(Statement declaration, Evaluation evaluation, Evaluation assignment, Statement statement) {
      this.loop = new ForStatement(declaration, evaluation, assignment, statement);
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(analyzer, loop, trace);
   }
   
   private static class ForStatement extends Statement {

      private final Evaluation condition;
      private final Statement declaration;
      private final Evaluation assignment;
      private final Statement body;

      public ForStatement(Statement declaration, Evaluation condition, Evaluation assignment, Statement body) {
         this.declaration = declaration;
         this.assignment = assignment;
         this.condition = condition;
         this.body = body;
      }
      
      @Override
      public Result execute(Scope scope) throws Exception {
         Scope compound = scope.getInner();
         
         declaration.execute(compound);
         
         while(true) {
            Value result = condition.evaluate(compound, null);
            Boolean value = result.getBoolean();
            
            if(value.booleanValue()) {
               Result next = body.execute(compound);
               
               if(next.isReturn() || next.isThrow()) {
                  return next;
               }
               if(next.isBreak()) {
                  return ResultType.getNormal();
               }
            } else {
               return ResultType.getNormal();
            } 
            if(assignment != null) {
               assignment.evaluate(compound, null);
            }
         }
      }
   }
}