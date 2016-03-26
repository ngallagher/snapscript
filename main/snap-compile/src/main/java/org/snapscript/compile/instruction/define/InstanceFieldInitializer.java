package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.Instance;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class InstanceFieldInitializer extends Initializer {
   
   private final NameExtractor extractor;
   private final Evaluation evaluation;
   
   public InstanceFieldInitializer(TextLiteral literal, Evaluation evaluation){
      this.extractor = new NameExtractor(literal);
      this.evaluation = evaluation;
   }

   @Override
   public Result execute(Scope scope, Type type) throws Exception {
      if(evaluation != null) {
         Instance instance = (Instance)scope;
         String name = extractor.extract(scope);
         Scope primitive = instance.getScope();
         State state = primitive.getState();
         Value value = state.getValue(name);
         
         if(value == null) {
            evaluation.evaluate(primitive, null);
         }
      }
      return ResultType.getNormal();
   }
}