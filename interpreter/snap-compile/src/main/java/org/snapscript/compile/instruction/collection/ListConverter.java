package org.snapscript.compile.instruction.collection;

import java.util.List;

public class ListConverter {

   private final ArrayConverter converter;
   
   public ListConverter() {
      this.converter = new ArrayConverter();
   }
   
   public boolean accept(Object value) throws Exception {
      if(value != null) {
         Class type = value.getClass();
         
         if(type.isArray()) {
            return true;
         }
         if(List.class.isAssignableFrom(type)) {
            return true;
         }
      }
      return false;
   }
   
   public List convert(Object value) throws Exception {
      if(value != null) {
         Class type = value.getClass();
         
         if(type.isArray()) {
            return converter.convert(value);
         }
         if(List.class.isAssignableFrom(type)) {
            return (List)value;
         }
         throw new IllegalArgumentException("The " + type + " is not a list");
      }
      return null;
   }
}
