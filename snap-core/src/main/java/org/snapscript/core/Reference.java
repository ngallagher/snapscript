package org.snapscript.core;

public class Reference extends Value {
   
   private Object value;
   private String name;
   private Type type;
   
   public Reference(Object value) {
      this(value, null, null);
   }
   
   public Reference(Object value, String name) {
      this(value, null, name);
   }
   
   public Reference(Object value, Type type) {
      this(value, type, null);
   }
   
   public Reference(Object value, Type type, String name) {
      this.value = value;
      this.name = name;
      this.type = type;
   }

   public String getName(){
      return name;
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
}