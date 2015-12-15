package org.snapscript.core;

public class Result {
   
   private final ResultType type;
   private final Object value;

   public Result() {
      this(ResultType.NORMAL);
   }

   public Result(ResultType type) {
      this(type, null);
   }

   public Result(ResultType type, Object value) {
      this.value = value;
      this.type = type;
   }

   public ResultType getType() {
      return type;
   }

   public <T> T getValue() {
      return (T)value;
   }
}