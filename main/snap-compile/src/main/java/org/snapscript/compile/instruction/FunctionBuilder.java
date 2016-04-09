package org.snapscript.compile.instruction;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class FunctionBuilder {

   private final Statement statement;
   
   public FunctionBuilder(Statement statement) {
      this.statement = statement;
   }

   public Function create(Signature signature, Type constraint, String name) {
      Invocation invocation = new StatementInvocation(signature, statement, constraint);
      return new InvocationFunction(signature, invocation, null, constraint, name, 0);
   }
}