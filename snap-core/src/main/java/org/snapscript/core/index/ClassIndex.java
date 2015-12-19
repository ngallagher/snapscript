package org.snapscript.core.index;

import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Type;

public class ClassIndex {
   
   private final AtomicReference<Type> reference;
   private final ClassIndexer indexer;
   private final Class require;
   
   public ClassIndex(ClassIndexer indexer, Class require) {      
      this.reference = new AtomicReference<Type>();
      this.indexer = indexer;
      this.require = require;
   }

   public Type getType() {
      Type type = reference.get();
      
      if(type == null) {
         try {
            type = indexer.index(require);
         } catch(Exception e) {
            throw new IllegalStateException("Could not load " + require, e);
         }
         reference.set(type);
      }
      return type;
   } 
}
