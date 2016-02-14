package org.snapscript.compile.instruction.collection;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.RandomAccess;

import org.snapscript.core.InternalArgumentException;

public class PrimitiveNumberList extends AbstractList<Object> implements RandomAccess {

   private final Object array;
   private final Class type;
   private final int length;

   public PrimitiveNumberList(Object array, Class type) {
      this.length = Array.getLength(array);
      this.array = array;
      this.type = type;
   }

   @Override
   public int size() {
      return length;
   }

   @Override
   public Object[] toArray() {
      Object instance = Array.newInstance(type, length);
      Object[] copy = (Object[])instance;
      
      for(int i = 0; i < length; i++) {
         copy[i] = Array.get(array, i);
      }
      return copy;
   }

   @Override
   public <T> T[] toArray(T[] copy) {
      Class type = copy.getClass();
      int require = copy.length;
     
      for(int i = 0; i < length && i < require; i++) {
         Number number = (Number)Array.get(array, i);
         Object value = number;
         
         if(type == Byte[].class) {
            value = number.byteValue();
         } else if(type == Double[].class) {
            value = number.doubleValue();
         } else if(type == Integer[].class) {
            value = number.intValue();
         } else if(type == Long[].class) {
            value = number.longValue();
         } else if(type == Short[].class) {
            value = number.shortValue();
         } else if(type == Float[].class) {
            value = number.shortValue();
         } else if(type == String[].class) {
            value = number.toString();
         } else if(type == Object[].class) {
            value = number;
         } else {
            throw new InternalArgumentException("Incompatible array type");
         }
         copy[i] = (T)value;
      }
      return copy;
   }

   @Override
   public Object get(int index) {
      return Array.get(array, index);
   }
  
   @Override
   public boolean add(Object element) {
      throw new InternalArgumentException("Array cannot be resized");
   }
   
   @Override
   public void add(int index, Object element) {
      throw new InternalArgumentException("Array cannot be resized");
   }

   @Override
   public Object set(int index, Object value) {
      Object previous = Array.get(array, index);
      Class entry = value.getClass();
      
      if(entry == String.class) {
         String text = (String)value;
         
         if(type == Byte.class) {
            value = Byte.parseByte(text);
         } else if(type == Double.class) {
            value = Double.parseDouble(text);
         } else if(type == Integer.class) {
            value = Integer.parseInt(text);
         } else if(type == Long.class) {
            value = Long.parseLong(text);
         } else if(type == Short.class) {
            value = Short.parseShort(text);
         } else if(type == Float.class) {
            value = Float.parseFloat(text);
         } else {
            throw new InternalArgumentException("Incompatible value type");
         }
         Array.set(array, index, value);
      } else {
         Number number = (Number)value;
         
         if(type == Byte.class) {
            value = number.byteValue();
         } else if(type == Double.class) {
            value = number.doubleValue();
         } else if(type == Integer.class) {
            value = number.intValue();
         } else if(type == Long.class) {
            value = number.longValue();
         } else if(type == Short.class) {
            value = number.shortValue();
         } else if(type == Float.class) {
            value = number.shortValue();
         } else {
            throw new InternalArgumentException("Incompatible value type");
         }
         Array.set(array, index, value);
      }
      return previous;
   }

   @Override
   public int indexOf(Object object) {
      Class type = object.getClass();
      
      for (int i = 0; i < length; i++) {
         Number number = (Number)Array.get(array, i);
         Object value = number;
         
         if(type == Byte.class) {
            value = number.byteValue();
         } else if(type == Double.class) {
            value = number.doubleValue();
         } else if(type == Integer.class) {
            value = number.intValue();
         } else if(type == Long.class) {
            value = number.longValue();
         } else if(type == Short.class) {
            value = number.shortValue();
         } else if(type == Float.class) {
            value = number.shortValue();
         } else if(type == String.class) {
            value = number.toString();
         } else {
            throw new InternalArgumentException("Incompatible value type");
         }
         if (object.equals(value)) {
            return i;
         }
      }
      return -1;
   }

   @Override
   public boolean contains(Object o) {
      int index = indexOf(o);

      if (index == -1) {
         return false;
      }
      return true;

   }
}
