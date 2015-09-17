package org.snapscript.core;


public abstract class Statement {
   
   public Result compile(Scope scope) throws Exception {
      return new Result();
   }
   
   public Result execute(Scope scope) throws Exception {
      return new Result();
   }
}