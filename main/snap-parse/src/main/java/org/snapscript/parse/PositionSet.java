package org.snapscript.parse;

import org.snapscript.common.ArrayQueue;
import org.snapscript.common.ArraySet;

public class PositionSet {

   private final ArrayQueue queue;
   private final ArraySet table;
   private final Object[] cache;
   private final int capacity;
   
   public PositionSet(int capacity) {
      this.cache = new Object[capacity * 20];
      this.queue = new ArrayQueue(cache, capacity * 19, capacity);
      this.table = new ArraySet(cache, 0, capacity * 19);
      this.capacity = capacity;
   }
   
   public boolean add(Long key) {
      if(table.add(key)) {
         int size = queue.size();
         
         if(size >= capacity) {
            Object last = queue.poll();
            
            if(last != null) {
               table.remove(last);
            }
         }
         queue.offer(key);
         return true;
      }
      return false;
   }
   
   public boolean contains(Long value) {
      return table.contains(value);
   }
   
   public int size() {
      return table.size();
   }
}
