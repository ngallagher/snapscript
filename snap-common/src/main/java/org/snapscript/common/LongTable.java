package org.snapscript.common;

public class LongTable<T> {

   private Object[] values;
   private long[] table;
   private int length;
   private int start;
   private int size;
   
   public LongTable(long[] table, int start, int length) {
      this.values = new Object[length];
      this.length = length;
      this.start = start;
      this.table = table;
   }
   
   public T get(long key) {
      if(key == 0) {
         throw new IllegalArgumentException("Key must not be zero");
      }
      int begin = index(key);
      
      for(int i = 0; i < length; i++) {
         int index = (begin + i) % length; 
         long current = table[start + index];
      
         if(current == key) {
            return (T)values[index];
         }
         if(current == 0) {
            return null;
         }
      }
      return null;
   }
   
   public T put(long key, T value) {
      if(key == 0) {
         throw new IllegalArgumentException("Key must not be zero");
      }
      int begin = index(key);
      
      for(int i = 0; i < length; i++) {
         int index = (begin + i) % length; // wrap around table
         long current = table[start + index];
      
         if(current == key) {
            T previous = (T)values[index];
            
            values[start +index] = value;
            return previous;
         }
         if(current == 0) {
            values[index] = value;
            table[start + index] = key; // slot was empty
            size++;
            return null;
         }
      }
      return null;
   }
   
   public boolean contains(long key) {
      if(key == 0) {
         throw new IllegalArgumentException("Key must not be zero");
      }
      int begin = index(key);
      
      for(int i = 0; i < length; i++) {
         int index = (begin + i) % length; 
         long current = table[start + index];
      
         if(current == key) {
            return true;
         }
         if(current == 0) {
            return false;
         }
      }
      return false;
   }
   
   public boolean remove(long key) {
      if(key == 0) {
         throw new IllegalArgumentException("Key must not be zero");
      }
      int begin = index(key);
      
      for(int i = 0; i < length; i++) {
         int index = (begin + i) % length; // next slot and wrap around
         long current = table[start + index];
      
         if(current == key) {
            values[index] = null; // enable gc
            table[start + index] = 0; // clear the slot
            begin = index + 1;
            size--;
            
            for(int j = 0; j < length; j++) { // make sure we rehash
               index = (begin + j) % length; // slot after match
               current = table[start + index];

               if(current == 0) {
                  return true; // no more slots to rehash
               }
               T previous = (T)values[index];
               
               values[index] = null; // enable gc
               table[start + index] = 0; // clear slot
               size--; // decrease size
               put(current, previous); // add to fill up space
            }
            return true;
         }
         if(current == 0) {
            return false;
         }
      }
      return false;
   }
   
   private int index(long value) {
      int hash = (int)(value ^ (value >>> 32));
      
      hash ^= hash >> 16; // murmur hash 3 integer finalizer 
      hash *= 0x85ebca6b;
      hash ^= hash >> 13;
      hash *= 0xc2b2ae35;
      hash ^= hash >> 16;
      
      return hash & (length-1);
   }
   
   public int size() {
      return size;
   }
}
