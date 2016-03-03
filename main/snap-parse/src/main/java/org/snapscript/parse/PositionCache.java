package org.snapscript.parse;

import java.util.Map;

import org.snapscript.common.LeastRecentlyUsedMap;

public class PositionCache<T> {
   
   private final Map<Long, T> cache;
   
   public PositionCache(int capacity) {
      this.cache = new LeastRecentlyUsedMap<Long, T>(capacity);
   }
   
   public synchronized T fetch(Long position) {
      return cache.get(position);
   }
   
   public synchronized void cache(Long position, T value) {
      cache.put(position, value);
   }
   
   public synchronized boolean contains(Long position) {
      return cache.containsKey(position);
   }
   
   public synchronized int size() {
      return cache.size();
   }
}
