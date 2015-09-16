package org.snapscript.core;

public class Constant extends Value {
   
   private final String name;
   private final Object object;
   
   public Constant(Object object) {
      this(object, null);
   }
   
   public Constant(Object object, String name) {
      this.name = name;
      this.object = object;
   }
   
   public String getName(){
      return name;
   }
   
   @Override
   public Object getValue(){
      return object;
   }
   
   @Override
   public void setValue(Object value){
      throw new IllegalStateException("Modification of constant '" + name + "'");
   } 
}
