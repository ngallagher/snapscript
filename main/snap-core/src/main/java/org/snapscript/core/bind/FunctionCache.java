package org.snapscript.core.bind;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.snapscript.core.Function;

public class FunctionCache { // copy on write cache

   private volatile Map<Object, Function> cache;
   private volatile MapUpdater updater; 
   
   public FunctionCache() {
      this.cache = new HashMap<Object, Function>();
      this.updater = new MapUpdater();
   }
   
   public boolean contains(Object key) {
      return cache.containsKey(key);
   }
   
   public Function fetch(Object key) {
      return cache.get(key);
   }
   
   public void cache(Object key, Function function) {
      updater.update(key, function);
   }
   
   private class MapUpdater {
      
      private final Lock lock;
      
      public MapUpdater() {
         this.lock = new ReentrantLock();
      }
      
      public void update(Object key, Function function) {
         lock.lock();
         
         try {
            Map<Object, Function> local = new HashMap<Object, Function>(cache);
            
            local.put(key, function);
            cache = local;
         } finally {
            lock.unlock();
         }
      }
   }
}
