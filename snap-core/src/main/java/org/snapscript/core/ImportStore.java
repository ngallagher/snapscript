package org.snapscript.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImportStore {

   private final Map<String, Type> store;
   
   public ImportStore() {
      this.store = new ConcurrentHashMap<String, Type>();
   }
   
   public Type get(String type) {
      return store.get(type);
   }
   
   public void store(String name, Type type) {
      store.put(name, type);
   }
   
   public void remove(String name) {
      store.remove(name);
   }
   
   public boolean contains(String name) {
      return store.containsKey(name);
   }
}
