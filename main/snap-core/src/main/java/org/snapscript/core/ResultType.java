package org.snapscript.core;

public enum ResultType{
   RETURN,
   THROW,
   BREAK,
   CONTINUE,
   NORMAL;
   
   public static Result getNormal(){
      return Result.NORMAL_RESULT;
   }
   
   public static Result getNormal(Object value) {
      return new Result(NORMAL, value);
   }
   
   public static Result getReturn(){
      return Result.RETURN_RESULT;
   }
   
   public static Result getReturn(Object value) {
      return new Result(RETURN, value);
   }
   
   public static Result getBreak() {
      return Result.BREAK_RESULT;
   }
   
   public static Result getContinue() {
      return Result.CONTINUE_RESULT;
   }
   
   public static Result getThrow(Object value) {
      return new Result(THROW, value);
   }
}