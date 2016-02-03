package org.snapscript.compile.instruction.define;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;

public class TraitFunctionBuilder {

   private final Statement statement;

   public TraitFunctionBuilder(Statement statement) {
      this.statement = statement;
   }
   
   public Function create(Signature signature, Scope scope, String name, int modifiers) throws Exception {
      Invocation invocation = new InstanceInvocation(statement, signature);
      Invocation scopeCall = new BoundaryInvocation(invocation, scope); // ensure the static stuff is in scope
      
      return new Function(signature, scopeCall, name, modifiers);// description is wrong here.....
   }
}
