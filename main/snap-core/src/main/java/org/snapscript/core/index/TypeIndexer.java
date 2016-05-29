package org.snapscript.core.index;

import java.util.LinkedHashMap;
import java.util.Map;

import org.snapscript.core.ImportScanner;
import org.snapscript.core.ModuleRegistry;
import org.snapscript.core.Type;
import org.snapscript.core.extend.ClassExtender;

public class TypeIndexer {

   private final Map<Object, Type> types;
   private final ModuleRegistry registry;
   private final ImportScanner scanner;
   private final ClassIndexer indexer;

   public TypeIndexer(ModuleRegistry registry, ImportScanner scanner, ClassExtender extender) {
      this.indexer = new ClassIndexer(this, registry, scanner, extender);
      this.types = new LinkedHashMap<Object, Type>();
      this.scanner = scanner;
      this.registry = registry;
   }
   
   public synchronized Type loadType(String type) throws Exception {
      Type done = types.get(type);

      if (done == null) {
         Class match = scanner.importType(type);
         
         if (match == null) {
            return null;
         }
         return loadType(match);
      }
      return done;
   }

   public synchronized Type loadType(String module, String name) throws Exception {
      String alias = createName(module, name);
      Type done = types.get(alias);

      if (done == null) {
         Class match = scanner.importType(alias);
         
         if (match == null) {
            return null;
         }
         return loadType(match);
      }
      return done;
   }

   public synchronized Type defineType(String module, String name) throws Exception {
      String alias = createName(module, name);
      Type done = types.get(alias);

      if (done == null) {
         Class match = scanner.importType(alias);
         
         if (match == null) {
            Type type = createType(module, name);
            
            types.put(type, type);
            types.put(alias, type);
            
            return type;
         }
         return loadType(match);
      }
      return done;
   }

   public synchronized Type loadType(Class source) throws Exception {
      Type done = types.get(source);
      
      if (done == null) {
         String alias = scanner.importName(source);
         String absolute = source.getName();
         Type type = createType(source);

         types.put(source, type);
         types.put(alias, type);
         types.put(absolute, type);
         
         return type;
      }
      return done;
   }

   private synchronized Type createType(String module, String name) throws Exception {
      String alias = createName(module, name);
      Type type = types.get(alias);
      
      if(type == null) {
         return new ScopeType(registry, module, name);
      }
      return type;
   }
   
   private synchronized Type createType(Class source) throws Exception {
      String alias = scanner.importName(source);
      String name = source.getSimpleName();
      Type type = types.get(alias);
      
      if(type == null) {
         return new ClassType(indexer, source, name);
      }
      return type;
   }
   
   private synchronized String createName(String module, String name) {
      if(module != null) {
         int length = module.length();
         
         if(length > 0) {
            return module + "." + name;
         }
      }
      return name;
   }
}
