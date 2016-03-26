package org.snapscript.compile.instruction.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class StaticFieldInitializer extends Initializer {
   
   private final NameExtractor extractor;
   private final Evaluation evaluation;
   private final AtomicBoolean done;
   private final Scope scope;
   
   public StaticFieldInitializer(TextLiteral literal, Evaluation evaluation, Scope scope){
      this.extractor = new NameExtractor(literal);
      this.done = new AtomicBoolean();
      this.evaluation = evaluation;
      this.scope = scope;
   }

   @Override
   public Result compile(Scope instance, Type type) throws Exception {
      if(done.compareAndSet(false, true)) {
         String name = extractor.extract(scope);
         State state = scope.getState();
         Value value = state.getValue(name);
         
         if(value == null) {
            evaluation.evaluate(scope, null);
         }
      }
      return ResultType.getNormal();
   }
}