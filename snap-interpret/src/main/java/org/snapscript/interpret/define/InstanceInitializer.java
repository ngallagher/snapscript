package org.snapscript.interpret.define;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.interpret.Evaluation;

public class InstanceInitializer extends Statement {
   
   private final Evaluation evaluation;
   private final Type type;
   
   public InstanceInitializer(Evaluation evaluation, Type type){
      this.evaluation = evaluation;
      this.type = type;
   }

   @Override
   public Result execute(Scope instance) throws Exception {
      Type current = instance.getType();
      
      if(current == type) {
         evaluation.evaluate(instance, null);
      }
      return new Result();
   }
}