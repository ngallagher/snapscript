package org.snapscript.core.index;

import java.lang.reflect.Field;

import org.snapscript.core.Accessor;
import org.snapscript.core.InternalStateException;

public class FieldAccessor implements Accessor<Object>{

   private final Field field;
   
   public FieldAccessor(Field field){
      this.field = field;
   }
   
   @Override
   public Object getValue(Object source) {
      try {
         return field.get(source);
      } catch(Exception e) {
         throw new InternalStateException("Illegal access to " + field, e);
      }
   }

   @Override
   public void setValue(Object source, Object value) {
      try {
         field.set(source, value);
      } catch(Exception e) {
         throw new InternalStateException("Illegal access to " + field, e);
      }
   }

}
