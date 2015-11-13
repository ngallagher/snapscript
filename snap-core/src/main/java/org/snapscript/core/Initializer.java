package org.snapscript.core;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public abstract class Initializer {
   
   public Result compile(Scope scope, Type type) throws Exception {
      return new Result();
   }
   
   public Result execute(Scope scope, Type type) throws Exception {
      return new Result();
   }
}