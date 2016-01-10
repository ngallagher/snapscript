package org.snapscript.core.index;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ClassReference implements Type {

   private final ClassIndex index;
   private final Class type;
   private final String name;
   
   public ClassReference(ClassIndexer indexer, Class type, String name) {
      this.index = new ClassIndex(indexer, type);
      this.name = name;
      this.type = type;
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
   public Type getEntry() {
      return index.getType().getEntry();
   }

   @Override
   public String getModule() {
      return index.getType().getModule();
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public Class getType() {
      return type;
   }
   
   @Override
   public String toString() {
      return name;
   }

}
