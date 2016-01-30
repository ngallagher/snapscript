package org.snapscript.compile.instruction;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;

public class FunctionBuilder {

   private final Statement statement;
   
   public FunctionBuilder(Statement statement) {
      this.statement = statement;
   }

   public Function create(Signature signature, String name) {
      Invocation invocation = new StatementInvocation(statement, signature);
      return new Function(signature, invocation, name);
   }
}
