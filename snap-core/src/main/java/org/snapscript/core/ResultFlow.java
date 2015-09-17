package org.snapscript.core;

public enum ResultFlow{
   RETURN,
   THROW,
   BREAK,
   CONTINUE,
   NORMAL;
   
   public final Result result;
   
   private ResultFlow(){
      this.result = new Result(this);
   }
   
   public Result getResult(){
      return result;
   }
   
   public Result getResult(Object value) {
      return new Result(this, value);
   }
}