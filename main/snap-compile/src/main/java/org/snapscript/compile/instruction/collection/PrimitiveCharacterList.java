package org.snapscript.compile.instruction.collection;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.RandomAccess;

import org.snapscript.core.InternalArgumentException;

public class PrimitiveCharacterList extends AbstractList<Character> implements RandomAccess {

   private final char[] array;
   private final int length;

   public PrimitiveCharacterList(char[] array) {
      this.length = array.length;
      this.array = array;
   }

   @Override
   public int size() {
      return length;
   }

   @Override
   public Object[] toArray() {
      Object instance = Array.newInstance(Character.class, length);
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
         Character character = array[i];
         Object value = character;
         
         if(type == String[].class) {
            value = value.toString();
         } else if(type == Character[].class) {
            value = character;
         } else if(type == Object[].class) {
            value = character;
         } else {
            throw new InternalArgumentException("Incompatible array type");
         }
         copy[i] = (T)value;
      }
      return copy;
   }

   @Override
   public Character get(int index) {
      return array[index];
   }

   @Override
   public boolean add(Character element) {
      throw new InternalArgumentException("Array cannot be resized");
   }
   
   @Override
   public void add(int index, Character element) {
      throw new InternalArgumentException("Array cannot be resized");
   }
   
   @Override
   public Character set(int index, Character value) {
      Character previous = array[index];
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


