package org.snapscript.core;

public class Transient extends Value {
   
   private final Object object;
   private final Type type;
   
   public Transient(Object object) {
      this(object, null);
   }
   
   public Transient(Object object, Type type) {
      this.object = object;
      this.type = type;
   }
   
   @Override
   public Type getConstraint(){
      return type;
   }
   
   @Override
   public Object getValue(){
      return object;
   }
   
   @Override
   public void setValue(Object value){
      throw new IllegalStateException("Transient value modified");
   } 
}
