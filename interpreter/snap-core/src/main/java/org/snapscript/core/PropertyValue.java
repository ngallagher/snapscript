package org.snapscript.core;

public class PropertyValue extends Value {

   private final Accessor accessor;
   private final Object object;   
   private final String name;

   public PropertyValue(Accessor accessor, Object object, String name) {
      this.accessor = accessor;
      this.object = object;
      this.name = name;
   }

   public String getName(){
      return name;
   }
   
   @Override
   public <T> T getValue() {
      return (T)accessor.getValue(object);
   }

   @Override
   public void setValue(Object value) {
      accessor.setValue(object, value);
   }
   
   @Override
   public String toString() {
      return String.valueOf(object);
   }
}
