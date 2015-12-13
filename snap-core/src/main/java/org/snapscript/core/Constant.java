package org.snapscript.core;

public class Constant extends Value {
   
   private final Object value;
   private final String name;
   private final Type type;
   
   public Constant(Object value) {
      this(value, null, null);
   }
   
   public Constant(Object value, String name) {
      this(value, null, name);
   }
   
   public Constant(Object value, Type type) {
      this(value, type, null);
   }
   
   public Constant(Object value, Type type, String name) {
      this.name = name;
      this.value = value;
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
   public void setValue(Object value){
      throw new IllegalStateException("Modification of constant '" + name + "'");
   } 
}
