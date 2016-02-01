package org.snapscript.core;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

public class FinalFieldAccessor implements Accessor<Object>{

   private final AtomicReference<Object> reference;
   private final Field field;
   
   public FinalFieldAccessor(Field field){
      this.reference = new AtomicReference<Object>();
      this.field = field;
   }
   
   @Override
   public Object getValue(Object source) {
      try {
         Object value = reference.get();
         
         if(value == null) {
            value = field.get(source);
            reference.set(value);
         }
         return value;
      } catch(Exception e) {
         throw new IllegalStateException("Illegal access to " + field, e);
      }
   }

   @Override
   public void setValue(Object source, Object value) {
      throw new IllegalStateException("Illegal modification of final " + field);
   }

}
