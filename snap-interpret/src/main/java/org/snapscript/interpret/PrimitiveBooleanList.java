package org.snapscript.interpret;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.RandomAccess;

public class PrimitiveBooleanList extends AbstractList<Boolean> implements RandomAccess {

   private final Object array;
   private final Class type;
   private final int length;

   public PrimitiveBooleanList(Object array, Class type) {
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
     
      if(type != Character.class)
      for(int i = 0; i < length && i < require; i++) {
         Boolean flag = (Boolean)Array.get(array, i);
         Object value = flag;
         
         if(type == String[].class) {
            value = value.toString();
         } else if(type == Boolean[].class) {
            value = flag;
         } else if(type == Object[].class) {
            value = flag;
         } else {
            throw new IllegalArgumentException("Incompatible array type");
         }
         copy[i] = (T)value;
      }
      return copy;
   }

   @Override
   public Boolean get(int index) {
      return (Boolean)Array.get(array, index);
   }

   @Override
   public Boolean set(int index, Boolean value) {
      Object previous = Array.get(array, index);
      Boolean result = (Boolean)previous;
      Array.set(array, index, value);
      return result;
   }

   @Override
   public int indexOf(Object object) {
      Class type = object.getClass();
      
      for (int i = 0; i < length; i++) {
         Object value = Array.get(array, i);

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

