package org.snapscript.compile.instruction.define;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class ThisInitializer extends Initializer {
   
   private final Evaluation expression;
   
   public ThisInitializer(Evaluation expression) {
      this.expression = expression;
   }

   @Override
   public Result execute(Scope instance, Type real) throws Exception {
      Value value = expression.evaluate(instance, instance);
      Scope result = value.getValue();
      
      return ResultType.getNormal(result); // this will return the instance created!!
   }
}