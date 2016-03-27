package org.snapscript.compile.instruction.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class StaticFieldInitializer extends Initializer {
   
   private final Evaluation evaluation;
   private final AtomicBoolean done;
   private final Scope scope;
   
   public StaticFieldInitializer(Evaluation evaluation, Scope scope){
      this.done = new AtomicBoolean();
      this.evaluation = evaluation;
      this.scope = scope;
   }

   @Override
   public Result compile(Scope instance, Type type) throws Exception {
      if(done.compareAndSet(false, true)) {
         evaluation.evaluate(scope, null);
      }
      return ResultType.getNormal();
   }
}