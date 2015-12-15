package org.snapscript.core;

public enum ResultType{
   RETURN,
   THROW,
   BREAK,
   CONTINUE,
   NORMAL;
   
   public final Result result;
   
   private ResultType(){
      this.result = new Result(this);
   }
   
   public boolean isReturn() {
      return this == RETURN;
   }
   
   public boolean isNormal() {
      return this == NORMAL;
   }
   
   public boolean isBreak() {
      return this == BREAK;
   }
   
   public boolean isThrow()  {
      return this == THROW;
   }
   
   public boolean isContinue() {
      return this == CONTINUE;
   }
   
   public static Result getNormal(){
      return new Result(NORMAL);
   }
   
   public static Result getNormal(Object value) {
      return new Result(NORMAL, value);
   }
   
   public static Result getReturn(){
      return new Result(RETURN);
   }
   
   public static Result getReturn(Object value) {
      return new Result(RETURN, value);
   }
   
   public static Result getBreak() {
      return new Result(BREAK);
   }
   
   public static Result getContinue() {
      return new Result(CONTINUE);
   }
   
   public static Result getThrow(Object value) {
      return new Result(THROW, value);
   }
}