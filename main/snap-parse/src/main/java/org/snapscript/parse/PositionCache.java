package org.snapscript.parse;

import org.snapscript.common.ArrayQueue;
import org.snapscript.common.ArrayTable;

public class PositionCache<T> {
   
   private final ArrayTable<T> table;
   private final ArrayQueue queue;
   private final Object[] cache;
   private final int capacity;
   
   public PositionCache(int capacity) {
      this.cache = new Object[capacity * 20];
      this.queue = new ArrayQueue(cache, capacity * 19, capacity);
      this.table = new ArrayTable<T>(cache, 0, capacity * 19);
      this.capacity = capacity;
   }
   
   public T fetch(long key) {
      return table.get(key);
   }
   
   public void cache(long key, T value) {
      T previous = table.put(key, value);
      
      if(previous == null) {
         int size = queue.size();
         
         if(size >= capacity) {
            Object last = queue.poll();
            
            if(last != null) {
               table.remove(last);
            }
         }
         queue.offer(key);
      }
   }
   
   public boolean contains(long key) {
      return table.contains(key);
   }
   
   public int size() {
      return table.size();
   }
}
