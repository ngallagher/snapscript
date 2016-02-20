package org.snapscript.compile.instruction;

import static org.snapscript.core.Reserved.METHOD_CLOSURE;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;

public class ClosureBuilder {

   private final Statement statement;
   
   public ClosureBuilder(Statement statement) {
      this.statement = statement;
   }

   public Function create(Signature signature, Scope scope) {
      Invocation invocation = new ClosureInvocation(statement, signature, scope);
      return new Function(signature, invocation, null, METHOD_CLOSURE, 0);
   }
}