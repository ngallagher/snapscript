package org.snapscript.compile.instruction;

import java.lang.reflect.Field;

import org.snapscript.core.Value;

public class MemberReference extends Value {
   
   private final Object object;
   private final Field field;
   private final Class type;
   private final String name;
   
   public MemberReference(Object object, String name) throws Exception {
      this.type = object.getClass();
      this.field = type.getDeclaredField(name);
      this.object = object;
      this.name = name;
   }
   
   @Override
   public Class getType() {
      return field.getType();
   }
   
   @Override
   public Object getValue(){
      try {
         field.setAccessible(true);
         return field.get(object);
      } catch(Exception e) {
         throw new IllegalStateException("Illegal access of " + name+ " on " + type);
      }
   }
   
   @Override
   public void setValue(Object value){
      try {
         field.setAccessible(true);
         field.set(object, value);
      } catch(Exception e) {
         throw new IllegalStateException("Illegal modification of " + name+ " on " + type);
      }                
   } 
}