package org.snapscript.core;

import java.lang.reflect.Method;

public class MethodAccessor implements Accessor<Object> {

   private final ParameterConverter converter;
   private final Method write;
   private final Method read;

   public MethodAccessor(Class type, Method read){
      this(type, read, null);
   }
   
   public MethodAccessor(Class type, Method read, Method write){
      this.converter = new ParameterConverter(type);
      this.write = write;
      this.read = read;
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
         if(value != null){
            value = converter.convert(value);
         }
         write.invoke(source, value);
      } catch(Exception e) {
         throw new IllegalStateException("Illegal access to " + write, e);
      }
   }
   
   private static class ParameterConverter {
      
      private final Class type;
      
      public ParameterConverter(Class type) {
         this.type = type;
      }
      
      public Object convert(Object value) {
         Class actual = value.getClass();
         Class parent = actual.getSuperclass();
         
         if(type == String.class) {
            return String.valueOf(value);
         }
         if(parent == Number.class) {
            Number number = (Number)value;
            
            if(type == Double.class) {
               return number.doubleValue();
            }
            if(type == Integer.class) {
               return number.intValue();
            }
            if(type == Float.class) {
               return number.floatValue();
            }
            if(type == Long.class) {
               return number.longValue();
            }
            if(type == Short.class) {
               return number.shortValue();
            }
            if(type == Byte.class) {
               return number.byteValue();
            }
         }
         return value;
      }
   }
}
