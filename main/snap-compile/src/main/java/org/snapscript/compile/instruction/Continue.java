package org.snapscript.compile.instruction;

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
import org.snapscript.parse.StringToken;

public class Continue implements Compilation {
   
   private final Statement control;
   
   public Continue(StringToken token){
      this.control = new ContinueStatement();
   }   
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(analyzer, control, trace);
   }
   
   private static class ContinueStatement extends Statement {
  
      private final Result result;
      
      public ContinueStatement(){
         this.result = ResultType.getContinue();
      }      
      
      @Override
      public Result execute(Scope scope) throws Exception {
         return result;
      }
   }
}