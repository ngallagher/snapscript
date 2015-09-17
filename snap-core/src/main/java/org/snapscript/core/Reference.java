package org.snapscript.core;

public class Reference extends Value {
   
   private Object value;
   private String name;
   
   public Reference(Object value) {
      this(value, null);
   }
   
   public Reference(Object value, String name) {
      this.value = value;
      this.name = name;
   }

   public String getName(){
      return name;
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