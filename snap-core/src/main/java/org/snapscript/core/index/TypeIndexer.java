package org.snapscript.core.index;

import java.util.LinkedHashMap;
import java.util.Map;

import org.snapscript.core.Bug;
import org.snapscript.core.ImportScanner;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.Type;

@Bug("This is rubbish and needs to be cleaned up")
public class TypeIndexer {

   private final Map<Object, Type> types;
   private final ModuleBuilder builder;
   private final ImportScanner scanner;
   private final ClassIndexer indexer;
   private final TypeCache cache;

   public TypeIndexer(ModuleBuilder builder, ImportScanner scanner) {
      this.types = new LinkedHashMap<Object, Type>();
      this.cache = new TypeCache();
      this.indexer = new ClassIndexer(builder, this, cache);
      this.scanner = scanner;
      this.builder = builder;
   }

   private void registerType(Object name, Type type) {
      //if (types.containsKey(name)) {
      //   throw new IllegalStateException("Key " + name + " already registered");
      //}
      types.put(name, type);
   }

   private Type resolveType(Object name) {
      return types.get(name);
   }

   private Type defineType(String moduleName, String name) throws Exception {
      String full = createName(moduleName, name);
      Type type = resolveType(full);

      if (type == null) {
         Module module = builder.create(moduleName);

         // if(module == null) {
         // throw new
         // IllegalArgumentException("Module '"+moduleName+"' does not exist");
         // }
         type = new ScopeType(module, name);
         registerType(full, type);
      }
      return type;
   }

   private String createName(String module, String name) {
      if (module != null && module.length() > 0) {
         return module + "." + name;
      }
      return name;
   }

   public Type loadType(String moduleName, String name) throws Exception {
      return loadType(moduleName, name, true); // XXX moduleName was null here?????
   }

   public Type loadType(String moduleName, String nameX, boolean create) throws Exception {
      String name = createName(moduleName, nameX);
      Type type = resolveType(name);

      if (type == null) {
         Class cls = scanner.importType(name);
         if (cls == null) {
            if (create) {
               return defineType(moduleName, nameX);
            }
            return null;
         }
         type = loadType(cls);
      }
      return type;
   }

   @Bug("Add to module")
   public Type loadType(final Class cls) throws Exception {
      Type done = resolveType(cls);
      if (done == null) {
         String name = scanner.importName(cls);
         String absolute = cls.getName();
         Type type = new ClassReference(indexer, cls, cls.getSimpleName());
         registerType(cls, type);
         registerType(name, type);
         registerType(absolute, type);

         return type;
      }
      return done;
   }

}
