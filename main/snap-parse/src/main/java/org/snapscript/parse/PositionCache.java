package org.snapscript.parse;

import java.util.Map;

import org.snapscript.common.LeastRecentlyUsedMap;

public class PositionCache<T> {
   
   private final Map<Integer, T> cache;
   
   public PositionCache(int capacity) {
      this.cache = new LeastRecentlyUsedMap<Integer, T>(capacity);
   }
   
   public T fetch(Integer position) {
      return cache.get(position);
   }
   
   public void cache(Integer position, T value) {
      cache.put(position, value);
   }
   
   public boolean contains(Integer position) {
      return cache.containsKey(position);
   }
   
   public int size() {
      return cache.size();
   }
}
