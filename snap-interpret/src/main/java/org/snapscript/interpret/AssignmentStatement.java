package org.snapscript.interpret;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class AssignmentStatement extends Statement {

   private final Evaluation identifier;
   private final Evaluation value;
   
   public AssignmentStatement(Evaluation identifier, Evaluation value) {
      this.identifier = identifier;
      this.value = value;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Value variable = identifier.evaluate(scope, null);
      Value result = value.evaluate(scope, null);
      State state = scope.getState();
      String name = variable.getString();
      
      state.setValue(name, result);
      return new Result();
   }
}