package org.snapscript.core;

public class Transient extends Value {
   
   private final String constraint;
   private final Object object;
   
   public Transient(Object object) {
      this(object, null);
   }
   
   public Transient(Object object, String constraint) {
      this.constraint = constraint;
      this.object = object;
   }
   
   @Override
   public String getConstraint(){
      return constraint;
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
