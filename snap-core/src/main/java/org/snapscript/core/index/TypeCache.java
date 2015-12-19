package org.snapscript.core.index;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.core.Type;

public class TypeCache {
   private final Map<Object, Type> types;
   
   public TypeCache(){
      this.types = new ConcurrentHashMap<Object, Type>(); 
   }

   public void registerType(Object name, Type type) {
      if(types.containsKey(name)) {
         throw new IllegalStateException("Key " + name + " already registered");
      }
      types.put(name, type);
   }
   public Type resolveType(Object name) {
      return types.get(name);
   }
}
