package org.snapscript.interpret;

import java.util.Map;
import java.util.Map.Entry;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

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
         return converter.convert(value);
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
         return map.entrySet();
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
         return (Iterable)value;
      }
   }
}
