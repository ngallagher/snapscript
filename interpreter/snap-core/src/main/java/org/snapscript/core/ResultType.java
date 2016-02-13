package org.snapscript.core;

public enum ResultType{
   RETURN,
   THROW,
   BREAK,
   CONTINUE,
   NORMAL;
   
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