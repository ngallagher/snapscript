package org.snapscript.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeExtractor {
   
   private final Map<Class, Type> types;
   private final TypeLoader loader;
   
   public TypeExtractor(TypeLoader loader) {
      this.types = new ConcurrentHashMap<Class, Type>();
      this.loader = loader;
   }
   
   public Type extract(Object value) throws Exception {
      if(value != null) {
         Class type = value.getClass();
         Type match = types.get(type);
         
         if(match == null) {
            if(SuperScope.class.isAssignableFrom(type)) {
               SuperScope scope = (SuperScope)value;
               return scope.getSuper();
            } 
            if(Scope.class.isAssignableFrom(type)) {
               Scope scope = (Scope)value;
               return scope.getType();
            } 
            Type actual = loader.loadType(type);
            
            if(actual != null) {
               types.put(type, actual);
            }
            return actual;
         }
         return match;
      }
      return null;
   }
}
