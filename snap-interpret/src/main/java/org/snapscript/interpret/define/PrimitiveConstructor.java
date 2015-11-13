package org.snapscript.interpret.define;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class PrimitiveConstructor extends Initializer {

   @Override
   public Result execute(Scope scope, Type type) throws Exception {
      CompoundScope instance= new CompoundScope(scope);      
      return new Result(ResultFlow.NORMAL,instance);
   }

}
