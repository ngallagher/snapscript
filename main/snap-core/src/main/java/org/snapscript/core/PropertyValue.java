package org.snapscript.core;

public class PropertyValue extends Value {

   private final Property property;
   private final Object object;   
   private final String name;

   public PropertyValue(Property property, Object object, String name) {
      if(property==null){
         new Exception(name).printStackTrace();
      }
      this.property = property;
      this.object = object;
      this.name = name;
   }

   public String getName(){
      return name;
   }
   
   @Override
   public <T> T getValue() {
      return (T)property.getValue(object);
   }

   @Override
   public void setValue(Object value) {
      property.setValue(object, value);
   }
   
   @Override
   public String toString() {
      return String.valueOf(object);
   }
}
