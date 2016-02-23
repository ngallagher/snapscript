package org.snapscript.core.index;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ClassPropertyBuilder {

   private final TypeIndexer indexer;
   
   public ClassPropertyBuilder(TypeIndexer indexer){
      this.indexer = indexer;
   }

   public List<Property> create(Class source) throws Exception {
      Type type = indexer.loadType(source);
      
      if(type == null) {
         throw new InternalStateException("Could not load type for " + source);
      }
      List<Property> properties = new ArrayList<Property>();
      Property thisProperty = new ThisProperty(type);
      Property classProperty = new ClassProperty(type);
      
      properties.add(thisProperty);
      properties.add(classProperty);
      
      return properties;        
   }
}
