package org.snapscript.parse;

import org.snapscript.common.LongQueue;
import org.snapscript.common.LongSet;

public class PositionSet {

   private final LongQueue queue;
   private final LongSet table;
   private final long[] cache;
   private final int capacity;
   
   public PositionSet(int capacity) {
      this.cache = new long[capacity * 20];
      this.queue = new LongQueue(cache, capacity * 19, capacity);
      this.table = new LongSet(cache, 0, capacity * 19);
      this.capacity = capacity;
   }
   
   public boolean add(long key) {
      if(table.add(key)) {
         int size = queue.size();
         
         if(size >= capacity) {
            long last = queue.poll();
            
            if(last != 0) {
               table.remove(last);
            }
         }
         queue.offer(key);
         return true;
      }
      return false;
   }
   
   public boolean contains(long value) {
      return table.contains(value);
   }
   
   public int size() {
      return table.size();
   }
}
