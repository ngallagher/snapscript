package org.snapscript.parse;

import org.snapscript.common.LongQueue;
import org.snapscript.common.LongTable;

public class PositionCache<T> {
   
   private final LongTable<T> table;
   private final LongQueue queue;
   private final long[] cache;
   private final int capacity;
   
   public PositionCache(int capacity) {
      this.cache = new long[capacity * 20];
      this.queue = new LongQueue(cache, capacity * 19, capacity);
      this.table = new LongTable<T>(cache, 0, capacity * 19);
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
            long last = queue.poll();
            
            if(last != 0) {
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
