package org.snapscript.core;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public abstract class Initializer {

   @Bug("All static intializers need to be FutureTasks")
   public Result compile(Scope scope, Type type) throws Exception { // static stuff
      return ResultType.getNormal();
   }
   
   public Result execute(Scope scope, Type type) throws Exception { // instance stuff
      return ResultType.getNormal();
   }
}