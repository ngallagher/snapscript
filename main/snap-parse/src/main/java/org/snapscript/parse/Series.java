package org.snapscript.parse;

import java.util.Iterator;

public class Series<T> implements Iterable<T> {
   
   private Object[] array;
   private int size;
   
   public Series() {
      super();
   }
   
   public boolean isEmpty() {
      return size == 0;
   }
   
   public T get(int index) {
      return (T)array[index];
   }
   
   @Override
   public Iterator<T> iterator() {
      return new SeriesIterator();
   }
   
   public void add(T value) {
      if(size == 0) {
         array = new Object[4];
      } else {
         if(size == array.length) {
            Object[] copy = new Object[size * 2];
            
            for(int i = 0; i< array.length; i++) {
               copy[i] = array[i];
            }
            array = copy;
         }
      }
      array[size++] = value;
   }

   public int size() {
      return size;
   }

   public void clear() {
      size = 0;
   }
   
   private class SeriesIterator implements Iterator<T> {
      
      private int index;

      @Override
      public boolean hasNext() {
         if(index >= size) {
            return false;
         }
         return true;
      }

      @Override
      public T next() {
         if(index >= size) {
            return null;
         }
         return (T)array[index++];
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException("Remove not supported");
      }
      
   }

}
