package org.snapscript.compile.instruction.operation;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class AssignmentStatement extends Statement {

   private final NameExtractor extractor;
   private final Evaluation value;
   
   public AssignmentStatement(Evaluation identifier, Evaluation value) {
      this.extractor = new NameExtractor(identifier);
      this.value = value;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Value result = value.evaluate(scope, null);
      String name = extractor.extract(scope);
      State state = scope.getState();
      
      state.setValue(name, result);
      
      return ResultType.getNormal();
   }
}