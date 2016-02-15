package org.snapscript.compile.instruction;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Statement;
import org.snapscript.core.StatementGroup;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;
import org.snapscript.parse.StringToken;

public class Compound implements Compilation {
   
   private final Statement body;
   
   public Compound(StringToken token) {
      this();
   }
   
   public Compound(Statement... statements) {
      this.body = new StatementGroup(statements);
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(analyzer, body, trace);
   }
}