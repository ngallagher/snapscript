package org.snapscript.interpret.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.interpret.Evaluation;

public class StaticInitializer extends Statement {
   
   private final Evaluation evaluation;
   private final AtomicBoolean done;
   private final Scope scope;
   
   public StaticInitializer(Evaluation evaluation, Scope scope){
      this.done = new AtomicBoolean();
      this.evaluation = evaluation;
      this.scope = scope;
   }

   @Override
   public Result execute(Scope instance) throws Exception {
      if(done.compareAndSet(false, true)) {
         evaluation.evaluate(scope, null);
      }
      return new Result();
   }
}