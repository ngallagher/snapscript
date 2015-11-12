package org.snapscript.interpret;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.convert.ProxyIterable;

public class IterationConverter {

   private final ArrayConverter converter;
   
   public IterationConverter() {
      this.converter = new ArrayConverter();
   }
   
   public Iteration convert(Object value) throws Exception {
      Class type = value.getClass();
   
      if(type.isArray()) {
         return new ArrayIteration(value);
      } 
      if (Iterable.class.isInstance(value)) {
         return new IterableIteration(value);
      } 
      if (Map.class.isInstance(value)) {
         return new MapIteration(value);
      }
      throw new IllegalArgumentException("Iteration for " + type + " is not possible");
   }
   
   private class ArrayIteration implements Iteration {
      
      private final Object value;
      
      public ArrayIteration(Object value) {
         this.value = value;
      }
      
      public Type getEntry(Scope scope) throws Exception {
         Module module = scope.getModule();
         Class type = value.getClass();
         Class entry = type.getComponentType();
         
         return module.getType(entry);
      }
      
      public Iterable getIterable(Scope scope) throws Exception {
         List list = converter.convert(value);
         
         if(!list.isEmpty()) {
            return new ProxyIterable(list);
         }
         return list;
      }
   }
   
   private class MapIteration implements Iteration {
      
      private final Object value;
      
      public MapIteration(Object value) {
         this.value = value;
      }
      
      @Override
      public Type getEntry(Scope scope) throws Exception {
         Module module = scope.getModule();
         return module.getType(Entry.class);
      }
      
      @Override
      public Iterable getIterable(Scope scope) throws Exception {
         Map map = (Map)value;
         Set set = map.entrySet();
         
         if(!set.isEmpty()) {
            return new ProxyIterable(set);
         }
         return set;
      }
   }
   
   private class IterableIteration implements Iteration {
      
      private final Object value;
      
      public IterableIteration(Object value) {
         this.value = value;
      }
      
      public Type getEntry(Scope scope) throws Exception {
         Module module = scope.getModule();
         return module.getType(Object.class);
      }
      
      public Iterable getIterable(Scope scope) throws Exception {
         Iterable iterable = (Iterable)value;
         return new ProxyIterable(iterable);
      }
   }
}
