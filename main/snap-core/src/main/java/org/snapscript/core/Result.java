package org.snapscript.core;

import static org.snapscript.core.ResultType.BREAK;
import static org.snapscript.core.ResultType.CONTINUE;
import static org.snapscript.core.ResultType.NORMAL;
import static org.snapscript.core.ResultType.RETURN;
import static org.snapscript.core.ResultType.THROW;

public class Result {
   
   public static final Result NORMAL_RESULT = new Result(NORMAL);
   public static final Result RETURN_RESULT = new Result(RETURN);
   public static final Result BREAK_RESULT = new Result(BREAK);
   public static final Result CONTINUE_RESULT = new Result(CONTINUE);
   public static final Result THROW_RESULT = new Result(THROW);
   
   private final ResultType type;
   private final Object value;

   public Result(ResultType type) {
      this(type, null);
   }

   public Result(ResultType type, Object value) {
      this.value = value;
      this.type = type;
   }
   
   public boolean isReturn() {
      return type == RETURN;
   }
   
   public boolean isNormal() {
      return type == NORMAL;
   }
   
   public boolean isBreak() {
      return type == BREAK;
   }
   
   public boolean isThrow()  {
      return type == THROW;
   }
   
   public boolean isContinue() {
      return type == CONTINUE;
   }

   public ResultType getType() {
      return type;
   }

   public <T> T getValue() {
      return (T)value;
   }
}