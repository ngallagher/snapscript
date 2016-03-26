package org.snapscript.compile.instruction.define;

import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class PrimitiveConstructor extends Initializer {
   
   private final PrimitiveInstanceBuilder builder;
   
   public PrimitiveConstructor(Type type) {
      this.builder = new PrimitiveInstanceBuilder(type);
   }

   @Override
   public Result execute(Scope scope, Type type) throws Exception {
      Scope instance = builder.create(scope);     
      return ResultType.getNormal(instance);
   }

}
