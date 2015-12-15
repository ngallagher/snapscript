package org.snapscript.compile.instruction.define;

import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class CompoundInitializer extends Initializer {
   
   private final Initializer[] initializers;
   
   public CompoundInitializer(Initializer... initializers) {
      this.initializers = initializers;
   }

   @Override
   public Result execute(Scope scope, Type type) throws Exception {
      Scope compound = scope.getInner();
      Result last = null;
      
      for(Initializer initializer : initializers) {
         Result result = initializer.execute(compound, type);
         ResultType flow = result.getType();
         
         if(!flow.isNormal()){
            return result;
         }
         last = result;
      }
      if(last == null) {
         return ResultType.getNormal();
      }
      return last;
   }

}
