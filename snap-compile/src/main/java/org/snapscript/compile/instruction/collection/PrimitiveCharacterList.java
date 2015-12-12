package org.snapscript.compile.instruction.collection;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.RandomAccess;

public class PrimitiveCharacterList extends AbstractList<Character> implements RandomAccess {

   private final Object array;
   private final Class type;
   private final int length;

   public PrimitiveCharacterList(Object array, Class type) {
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
         Character character = (Character)Array.get(array, i);
         Object value = character;
         
         if(type == String[].class) {
            value = value.toString();;
         } else if(type == Character[].class) {
            value = character;
         } else if(type == Object[].class) {
            value = character;
         } else {
            throw new IllegalArgumentException("Incompatible array type");
         }
         copy[i] = (T)value;
      }
      return copy;
   }

   @Override
   public Character get(int index) {
      return (Character)Array.get(array, index);
   }

   @Override
   public boolean add(Character element) {
      throw new IllegalArgumentException("Array cannot be resized");
   }
   
   @Override
   public void add(int index, Character element) {
      throw new IllegalArgumentException("Array cannot be resized");
   }
   
   @Override
   public Character set(int index, Character value) {
      Object previous = Array.get(array, index);
      Character result = (Character)previous;
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

