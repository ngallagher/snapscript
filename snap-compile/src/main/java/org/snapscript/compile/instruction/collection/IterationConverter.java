package org.snapscript.compile.instruction.collection;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
      if(Iterable.class.isInstance(value)) {
         return new IterableIteration(value);
      } 
      if(Map.class.isInstance(value)) {
         return new MapIteration(value);
      }
      if(Enumeration.class.isInstance(value)) {
         return new EnumerationIteration(value);
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
         
         if(value != null) {
            return new ProxyIterable(iterable);
         }
         return Collections.emptyList();
      }
   }
   
   private class EnumerationIteration implements Iteration {
      
      private final Object value;
      
      public EnumerationIteration(Object value) {
         this.value = value;
      }
      
      public Type getEntry(Scope scope) throws Exception {
         Module module = scope.getModule();
         return module.getType(Object.class);
      }
      
      public Iterable getIterable(Scope scope) throws Exception {
         Enumeration list = (Enumeration)value;
         
         if(list != null) {
            EnumerationIterable iterable = new EnumerationIterable(list);
            ProxyIterable proxy = new ProxyIterable(iterable);
            
            return proxy;
         }
         return Collections.emptyList();
      }
      
      private class EnumerationIterable implements Iterable {
         
         private final Enumeration list;
         
         public EnumerationIterable(Enumeration list) {
            this.list = list;
         }
         
         public Iterator iterator() {
            return new EnumerationIterator(list);
         }
      }
      
      private class EnumerationIterator implements Iterator {
         
         private final Enumeration list;
         
         public EnumerationIterator(Enumeration list) {
            this.list = list;
         }

         @Override
         public boolean hasNext() {
            return list.hasMoreElements();
         }

         @Override
         public Object next() {
            return list.nextElement();
         }
         
         @Override
         public void remove() {
            throw new UnsupportedOperationException("Remove not supported");
         }
      }
   }
}
