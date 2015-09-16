package org.snapscript.core;

import java.lang.reflect.Method;

public class MethodAccessor implements Accessor<Object> {

   private final Method read;
   private final Method write;
   private final Class type;

   public MethodAccessor(Class type, Method read){
      this(type, read, null);
   }
   
   public MethodAccessor(Class type, Method read, Method write){
      this.read = read;
      this.write = write;
      this.type = type;
   }
   
   @Override
   public Object getValue(Object source) {
      try {
         return read.invoke(source);
      } catch(Exception e) {
         throw new IllegalStateException("Illegal access to " + read, e);
      }
   }

   @Override
   public void setValue(Object source, Object value) {
      try {
         if(write == null) {
            throw new IllegalStateException("Illegal write for " + read);
         }
         //XXX conversion required!!!
         if(value!=null){
            if(type == Double.class && value.getClass() == Integer.class){
               value=((Integer)value).doubleValue();
            }
            if(type == Integer.class && value.getClass() == Double.class){
               value=((Double)value).intValue();
            }   
         }
         write.invoke(source, value);
      } catch(Exception e) {
         throw new IllegalStateException("Illegal access to " + write, e);
      }
   }
}
