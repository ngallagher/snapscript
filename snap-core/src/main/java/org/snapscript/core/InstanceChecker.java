package org.snapscript.core;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceChecker {

   private final Map<Type, Set<Type>> types;
   
   public InstanceChecker() {
      this.types = new ConcurrentHashMap<Type, Set<Type>>();
   }
   
   public boolean check(Type actual, Type required) {
      Set<Type> list = types.get(actual);
      
      if(list == null) {
         list = collect(actual);
      }
      return list.contains(required);
   }
   
   private Set<Type> collect(Type type) {
      Set<Type> list = new HashSet<Type>();
      
      if(type != null) {
         collect(type, list);
      }
      types.put(type, list);
      return list;
      
   }
   
   private Set<Type> collect(Type type, Set<Type> types) {
      if(types.add(type)) {
         List<Type> list = type.getTypes();
         
         for(Type entry : list) {
            collect(entry, types);
         }
      }
      return types;
   }
}
