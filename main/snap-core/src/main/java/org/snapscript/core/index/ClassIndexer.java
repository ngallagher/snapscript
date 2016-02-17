package org.snapscript.core.index;

import static org.snapscript.core.Reserved.DEFAULT_PACKAGE;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.ImportScanner;
import org.snapscript.core.InternalArgumentException;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleRegistry;
import org.snapscript.core.PrimitivePromoter;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ClassIndexer {

   private final HierarchyIndexer hierarchy;
   private final FunctionIndexer functions;
   private final PropertyIndexer properties;
   private final PrimitivePromoter promoter;
   private final ImportScanner scanner;
   private final ModuleRegistry registry;
   private final TypeIndexer indexer;

   public ClassIndexer(TypeIndexer indexer, ModuleRegistry registry, ImportScanner scanner) {
      this.properties = new PropertyIndexer(indexer);
      this.functions = new FunctionIndexer(indexer);
      this.hierarchy = new HierarchyIndexer(indexer);
      this.promoter = new PrimitivePromoter();
      this.scanner = scanner;
      this.registry = registry;
      this.indexer = indexer;
   }
   
   public List<Type> indexTypes(ClassType type) throws Exception {
      Class source = type.getType();
      Class actual = promoter.promote(source);
      
      if(actual == null) {
         throw new InternalArgumentException("Could not determine type for " + source);
      }
      return hierarchy.index(actual);
   }
   
   public List<Property> indexProperties(ClassType type) throws Exception {
      Class source = type.getType();
      Class actual = promoter.promote(source);
      
      if(actual == null) {
         throw new InternalArgumentException("Could not determine type for " + source);
      }
      return properties.index(actual);
   }
   
   public List<Function> indexFunctions(ClassType type) throws Exception {
      Class source = type.getType();
      Class actual = promoter.promote(source);
      
      if(actual == null) {
         throw new InternalArgumentException("Could not determine type for " + source);
      }
      return functions.index(type);
   }
   
   public Module indexModule(ClassType type) throws Exception {
      Class source = type.getType();
      Class actual = promoter.promote(source);
      
      if(actual == null) {
         throw new InternalArgumentException("Could not determine type for " + source);
      }
      Package module = actual.getPackage();
      
      if(module != null) {
         String name = scanner.importName(module);
         
         if(name != null) {
            return registry.addModule(name);
         }
      }
      return registry.addModule(DEFAULT_PACKAGE);
   }
   
   public Type indexEntry(ClassType type) throws Exception {
      Class source = type.getType();
      Class entry = source.getComponentType();
      
      if(entry != null) {
         Class actual = promoter.promote(entry);
         
         if(actual == null) {
            throw new InternalArgumentException("Could not determine type for " + source);
         }
         return indexer.loadType(actual);
      }
      return null;
   }
}
