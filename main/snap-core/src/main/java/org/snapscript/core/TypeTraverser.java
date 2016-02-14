package org.snapscript.core;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TypeTraverser {
   
   private final Map<Type, Set<Type>> types;
   
   public TypeTraverser() {
      this.types = new ConcurrentHashMap<Type, Set<Type>>();
   }

   public Set<Type> traverse(Type actual) {
      Set<Type> list = types.get(actual);
      
      if(list == null) {
         return collect(actual);
      }
      return list;
   }
   
   private Set<Type> collect(Type type) {
      Set<Type> list = new LinkedHashSet<Type>();
      
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
