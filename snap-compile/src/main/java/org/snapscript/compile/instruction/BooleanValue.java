package org.snapscript.compile.instruction;

import org.snapscript.core.Value;

public class BooleanValue extends Value {   
   
   public static final BooleanValue TRUE = new BooleanValue(true);
   public static final BooleanValue FALSE = new BooleanValue(false); 

   private final Boolean value;
   
   public BooleanValue(Boolean value) {
      this.value = value;
   }
   
   @Override
   public Class getType() {
      return Boolean.class;
   }     
   
   @Override
   public String getConstraint(){
      return Boolean.class.getSimpleName();
   } 
   
   @Override
   public Boolean getValue(){
      return value;
   }
   
   @Override
   public void setValue(Object value){
      throw new IllegalStateException("Illegal modification of value");
   }
}
