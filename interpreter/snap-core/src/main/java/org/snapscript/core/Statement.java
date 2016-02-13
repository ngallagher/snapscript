package org.snapscript.core;

public abstract class Statement {
                  
   public Result compile(Scope scope) throws Exception {
      return ResultType.getNormal();
   }
   
   public Result execute(Scope scope) throws Exception {
      return ResultType.getNormal();
   }
}