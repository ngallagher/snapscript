package org.snapscript.core.index;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ClassIndex {
   
   private List<Property> properties;
   private List<Function> functions;
   private ClassIndexer indexer;
   private List<Type> types;
   private ClassType require;
   private Module module;
   private Type entry;
   
   public ClassIndex(ClassIndexer indexer, ClassType require) {      
      this.indexer = indexer;
      this.require = require;
   }

   public List<Property> getProperties() {
      if(properties == null) {
         try {
            properties = indexer.indexProperties(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return properties;
   }
   
   public List<Function> getFunctions() {
      if(functions == null) {
         try {
            functions = indexer.indexFunctions(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return functions;
   }
   
   public List<Type> getTypes() {
      if(types == null) {
         try {
            types = indexer.indexTypes(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return types;
   }
   
   public Module getModule() {
      if(module == null) {
         try {
            module = indexer.indexModule(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return module;
   }

   public Type getEntry() {
      if(entry == null) {
         try {
            entry = indexer.indexEntry(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return entry;
   }
}
