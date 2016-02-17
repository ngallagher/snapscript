package org.snapscript.core.index;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ClassType implements Type {

   private final ClassIndex index;
   private final Class type;
   private final String name;
   
   public ClassType(ClassIndexer indexer, Class type, String name) {
      this.index = new ClassIndex(indexer, this);
      this.name = name;
      this.type = type;
   }
   
   @Override
   public List<Property> getProperties() {
      return index.getProperties();
   }

   @Override
   public List<Function> getFunctions() {
      return index.getFunctions();
   }

   @Override
   public List<Type> getTypes() {
      return index.getTypes();
   }

   @Override
   public Module getModule() {
      return index.getModule();
   }

   @Override
   public Type getEntry() {
      return index.getEntry();
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
