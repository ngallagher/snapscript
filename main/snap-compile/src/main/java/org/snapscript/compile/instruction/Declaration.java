package org.snapscript.compile.instruction;

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

public class Declaration implements Compilation {
   
   private final Statement declaration;   
   
   public Declaration(Evaluation declaration) {
      this.declaration = new DeclarationStatement(declaration);     
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(analyzer, declaration, trace);
   }
   
   private static class DeclarationStatement extends Statement {

      private final Evaluation declaration;   
      
      public DeclarationStatement(Evaluation declaration) {
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