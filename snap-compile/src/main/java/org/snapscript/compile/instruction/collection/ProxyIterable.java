package org.snapscript.compile.instruction.collection;

import java.util.Iterator;
import java.util.Map.Entry;

import org.snapscript.core.convert.ProxyBuilder;
import org.snapscript.core.convert.ProxyExtractor;

public class ProxyIterable implements Iterable {

   private final ProxyExtractor extractor;
   private final ProxyBuilder builder;
   private final Iterable iterable;
   
   public ProxyIterable(Iterable iterable) {
      this.extractor = new ProxyExtractor();
      this.builder = new ProxyBuilder();
      this.iterable = iterable;
   }
   
   @Override
   public Iterator iterator() {
      Iterator iterator = iterable.iterator();
      return new ProxyIterator(iterator);
   }
   
   private class ProxyIterator implements Iterator {
      
      private final Iterator iterator;
      
      public ProxyIterator(Iterator iterator) {
         this.iterator = iterator;
      }

      @Override
      public boolean hasNext() {
         return iterator.hasNext();
      }

      @Override
      public Object next() {
         Object value = iterator.next();
         
         if(value != null) {
            if(Entry.class.isInstance(value)) {
               return new ProxyEntry((Entry)value);
            }
         }
         return extractor.extract(value);
      }
      
   }
   
   private class ProxyEntry implements Entry {
      
      private final Entry entry;
      
      public ProxyEntry(Entry entry) {
         this.entry = entry;
      }
      
      @Override
      public Object getKey() {
         Object key = entry.getKey();
         
         if(key != null) {
            return extractor.extract(key);
         }
         return key;
      }
      
      @Override
      public Object getValue() {
         Object value = entry.getValue();
         
         if(value != null) {
            return extractor.extract(value);
         }
         return value;
      }

      @Override
      public Object setValue(Object value) { 
         Object proxy = builder.create(value);
         Object previous = entry.setValue(proxy);
         
         if(previous != null) {
            return extractor.extract(previous);
         }
         return previous;
      }
   }
}
