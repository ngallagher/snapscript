package org.snapscript.core;

public class Reference extends Value {
   
   private Object value;
   private Type type;
   
   public Reference(Object value) {
      this(value, null);
   }
   
   public Reference(Object value, Type type) {
      this.value = value;
      this.type = type;
   }
   
   @Override
   public Type getConstraint() {
      return type;
   }
   
   @Override
   public <T> T getValue() {
      return (T)value;
   }

   @Override
   public void setValue(Object value) {
      this.value = value;
   }
   
   @Override
   public String toString() {
      return String.valueOf(value);
   }
}