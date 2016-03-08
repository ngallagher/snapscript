package org.snapscript.compile.instruction.collection;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.RandomAccess;

import org.snapscript.core.InternalArgumentException;

public class PrimitiveByteList extends AbstractList<Object> implements RandomAccess {

   private final byte[] array;
   private final int length;
   
   public PrimitiveByteList(byte[] array) {
      this.length = array.length;
      this.array = array;
   }
   
   public int length() {
      return length;
   }

   @Override
   public int size() {
      return length;
   }

   @Override
   public Object[] toArray() {
      Object instance = Array.newInstance(Byte.class, length);
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
         Byte number = array[i];
         Object value = number;
         
         if(type != Byte[].class) {
            if(type == Short[].class) {
               value = number.shortValue();
            } else if(type == Double[].class) {
               value = number.doubleValue();
            } else if(type == Float[].class) {
               value = number.floatValue();
            } else if(type == Long[].class) {
               value = number.longValue();
            } else if(type == Integer[].class) {
               value = number.intValue();
            } else if(type == String[].class) {
               value = number.toString();
            } else if(type == Object[].class) {
               value = number;
            } else {
               throw new InternalArgumentException("Incompatible array type");
            }
         }
         copy[i] = (T)value;
      }
      return copy;
   }

   @Override
   public Object get(int index) {
      return array[index];
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
      Byte previous = array[index];
      Class entry = value.getClass();
      
      if(entry == String.class) {
         String text = (String)value;
         array[index] = Byte.parseByte(text);
      } else {
         Number number = (Number)value;
         array[index] = number.byteValue();
      }
      return previous;
   }

   @Override
   public int indexOf(Object object) {
      Class type = object.getClass();
      
      for (int i = 0; i < length; i++) {
         Byte number = array[i];
         Object value = number;
         
         if(type != Byte.class) {
            if(type == Float.class) {
               value = number.floatValue();
            } else if(type == Short.class) {
               value = number.shortValue();
            } else if(type == Double.class) {
               value = number.doubleValue();
            } else if(type == Long.class) {
               value = number.longValue();
            } else if(type == Integer.class) {
               value = number.intValue();
            } else if(type == String.class) {
               value = number.toString();
            } else {
               throw new InternalArgumentException("Incompatible value type");
            }
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
