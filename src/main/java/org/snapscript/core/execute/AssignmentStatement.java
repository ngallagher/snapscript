package org.snapscript.core.execute;

import org.snapscript.core.Scope;
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
      String name = variable.getString();
      
      scope.setValue(name, result);
      return new Result();
   }
}