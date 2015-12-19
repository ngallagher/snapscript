package org.snapscript.core.index;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ClassReference implements Type {

   private final ClassIndex index;
   
   public ClassReference(ClassIndexer indexer, Class actual) {
      this.index = new ClassIndex(indexer, actual);
   }
   
   @Override
   public List<Property> getProperties() {
      return index.getType().getProperties();
   }

   @Override
   public List<Function> getFunctions() {
      return index.getType().getFunctions();
   }

   @Override
   public List<Type> getTypes() {
      return index.getType().getTypes();
   }

   @Override
   public Class getType() {
      return index.getType().getType();
   }

   @Override
   public Type getEntry() {
      return index.getType().getEntry();
   }

   @Override
   public String getModule() {
      return index.getType().getModule();
   }

   @Override
   public String getName() {
      return index.getType().getName();
   }

   @Override
   public String toString() {
      return index.getType().toString();
   }

}
