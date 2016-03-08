package org.snapscript.compile.instruction.collection;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.RandomAccess;

import org.snapscript.core.InternalArgumentException;

public class PrimitiveBooleanList extends AbstractList<Boolean> implements RandomAccess {

   private final boolean[] array;
   private final int length;

   public PrimitiveBooleanList(boolean[] array) {
      this.length = array.length;
      this.array = array;
   }

   @Override
   public int size() {
      return length;
   }

   @Override
   public Object[] toArray() {
      Object instance = Array.newInstance(Boolean.class, length);
      Object[] copy = (Object[])instance;
      
      for(int i = 0; i < length; i++) {
         copy[i] = array[i];
      }
      return copy;
   }

   @Override
   public <T> T[] toArray(T[] copy) {
      Class type = copy.getClass();
      int require = copy.length;
     
      for(int i = 0; i < length && i < require; i++) {
         Boolean flag = array[i];
         Object value = flag;
         
         if(type == String[].class) {
            value = value.toString();
         } else if(type == Boolean[].class) {
            value = flag;
         } else if(type == Object[].class) {
            value = flag;
         } else {
            throw new InternalArgumentException("Incompatible array type");
         }
         copy[i] = (T)value;
      }
      return copy;
   }

   @Override
   public Boolean get(int index) {
      return array[index];
   }
   
   @Override
   public boolean add(Boolean element) {
      throw new InternalArgumentException("Array cannot be resized");
   }
   
   @Override
   public void add(int index, Boolean element) {
      throw new InternalArgumentException("Array cannot be resized");
   }

   @Override
   public Boolean set(int index, Boolean value) {
      Boolean result = array[index];
      array[index] = value;
      return result;
   }

   @Override
   public int indexOf(Object object) {
      for (int i = 0; i < length; i++) {
         Object value = array[i];

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