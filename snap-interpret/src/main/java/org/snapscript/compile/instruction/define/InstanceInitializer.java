package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class InstanceInitializer extends Initializer {
   
   private final Evaluation evaluation;
   private final Type type;
   
   public InstanceInitializer(Evaluation evaluation, Type type){
      this.evaluation = evaluation;
      this.type = type;
   }

   @Override
   public Result execute(Scope instance, Type type) throws Exception {
      Type current = instance.getType();
      
      // This check is probably rubbish!!!
      //if(current == type) {
         evaluation.evaluate(instance, null);
      //}
      return new Result();
   }
}