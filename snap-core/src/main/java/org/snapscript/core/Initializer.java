package org.snapscript.core;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public abstract class Initializer {
   
   public Result compile(Scope scope, Type type) throws Exception { // static stuff
      return new Result();
   }
   
   public Result execute(Scope scope, Type type) throws Exception { // instance stuff
      return new Result();
   }
}