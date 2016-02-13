package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_SUPER;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class SuperInitializer extends Initializer {
   
   private final SuperScopeBuilder builder;
   private final Evaluation expression;
   private final Type type;
   
   public SuperInitializer(Evaluation expression, Type type) {
      this.builder = new SuperScopeBuilder(type);
      this.expression = expression;
      this.type = type;
   }

   @Override
   public Result execute(Scope instance, Type real) throws Exception {
      Value reference = expression.evaluate(instance, real);
      Scope value = reference.getValue();
      Scope base = builder.create(value, real); 
      Value constant = ValueType.getConstant(base, type);
      State state = base.getState();
      
      state.addConstant(TYPE_SUPER, constant);
      
      return ResultType.getNormal(base);
   }
}