package org.snapscript.common;

public class LongSet {

   private long[] table;
   private int length;
   private int start;
   private int size;
   
   public LongSet(long[] table, int start, int length) {
      this.length = length;
      this.start = start;
      this.table = table;
   }
   
   public boolean add(long value) {
      if(value == 0) {
         throw new IllegalArgumentException("Value must not be zero");
      }
      int begin = index(value);
      
      for(int i = 0; i < length; i++) {
         int index = (begin + i) % length; // wrap around table
         long current = table[start + index];
      
         if(current == value) {
            return false; // already there
         }
         if(current == 0) {
            table[start + index] = value; // slot was empty
            size++;
            return true;
         }
      }
      return false;
   }
   
   public boolean contains(long value) {
      if(value == 0) {
         throw new IllegalArgumentException("Value must not be zero");
      }
      int begin = index(value);
      
      for(int i = 0; i < length; i++) {
         int index = (begin + i) % length; 
         long current = table[start + index];
      
         if(current == value) {
            return true;
         }
         if(current == 0) {
            return false;
         }
      }
      return false;
   }
   
   public boolean remove(long value) {
      if(value == 0) {
         throw new IllegalArgumentException("Value must not be zero");
      }
      int begin = index(value);
      
      for(int i = 0; i < length; i++) {
         int index = (begin + i) % length; // next slot and wrap around
         long current = table[start + index];
      
         if(current == value) {
            table[start + index] = 0; // clear the slot
            begin = index + 1;
            size--;
            
            for(int j = 0; j < length; j++) { // make sure we backfill
               index = (begin + j) % length; // slot after match
               current = table[start + index];

               if(current == 0) {
                  break;
               }
               table[start + index] = 0; // clear slot
               size--; // decrease size
               add(current); // add to fill up space
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
