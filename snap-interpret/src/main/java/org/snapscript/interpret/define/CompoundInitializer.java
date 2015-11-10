package org.snapscript.interpret.define;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class CompoundInitializer implements Initializer {
   
   private final Initializer[] initializers;
   
   public CompoundInitializer(Initializer... initializers) {
      this.initializers = initializers;
   }

   @Override
   public Result initialize(Scope scope, Type type) throws Exception {
      Scope compound = new CompoundScope(scope);
      Result last = new Result();
      
      for(Initializer initializer : initializers) {
         Result result = initializer.initialize(compound, type);
         ResultFlow flow = result.getFlow();
         
         if(flow != ResultFlow.NORMAL){
            return result;
         }
         last = result;
      }
      return last;
   }

}
