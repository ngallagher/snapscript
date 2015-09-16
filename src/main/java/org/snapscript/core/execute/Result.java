package org.snapscript.core.execute;

public class Result {
   
   private final ResultFlow flow;
   private final Object value;

   public Result() {
      this(ResultFlow.NORMAL);
   }

   public Result(ResultFlow flow) {
      this(flow, null);
   }

   public Result(ResultFlow flow, Object value) {
      this.flow = flow;
      this.value = value;
   }

   public ResultFlow getFlow() {
      return flow;
   }

   public <T> T getValue() {
      return (T)value;
   }
}