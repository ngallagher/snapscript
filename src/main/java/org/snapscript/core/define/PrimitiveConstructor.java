package org.snapscript.core.define;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Scope;
import org.snapscript.core.execute.Result;
import org.snapscript.core.execute.ResultFlow;
import org.snapscript.core.execute.Statement;

public class PrimitiveConstructor extends Statement{

   @Override
   public Result execute(Scope scope) throws Exception {
      CompoundScope instance= new CompoundScope(scope);      
      return new Result(ResultFlow.NORMAL,instance);
   }

}
