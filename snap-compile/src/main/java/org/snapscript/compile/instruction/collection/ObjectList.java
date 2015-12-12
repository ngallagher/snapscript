package org.snapscript.compile.instruction.collection;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.RandomAccess;

public class ObjectList extends AbstractList<Object> implements RandomAccess {

   private final Object[] array;
   private final Class type;
   private final int length;

   public ObjectList(Object[] array, Class type) {
      this.length = array.length;
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
         copy[i] = array[i];
      }
      return copy;
   }

   @Override
   public <T> T[] toArray(T[] copy) {
      Class type = copy.getClass();
      int require = copy.length;
     
      if(require >= length) {
         for(int i = 0; i < length; i++) {
            copy[i] = (T)array[i];
         }
      }
      return (T[])toArray();
   }

   @Override
   public Object get(int index) {
      return array[index];
   }
   
   @Override
   public boolean add(Object element) {
      throw new IllegalArgumentException("Array cannot be resized");
   }
   
   @Override
   public void add(int index, Object element) {
      throw new IllegalArgumentException("Array cannot be resized");
   }

   @Override
   public Object set(int index, Object value) {
      Object previous = array[index];
      array[index] = value;
      return previous;
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

