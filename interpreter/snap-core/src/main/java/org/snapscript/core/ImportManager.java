package org.snapscript.core;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ImportManager {
   
   private final Set<String> imports;
   private final Context context;
   private final String resource;
   
   public ImportManager(Context context, String resource) {
      this.imports = new CopyOnWriteArraySet<String>();
      this.resource = resource;
      this.context = context;
   }
   
   public void addImport(String name) {
      imports.add(name);
   }

   public Type getType(String name) {
      try {
         TypeLoader loader = context.getLoader();
         Type type = loader.resolveType(resource, name);
         
         if(type == null) {
            for(String module : imports) {
               type = loader.resolveType(module, name); // this is "tetris.game.*"
            }
            if(type == null) {
               type = loader.resolveType(null, name); // null is "java.*"
            }
            if(type == null) {
               ModuleRegistry registry = context.getRegistry();
               
               for(String module : imports) {
                  Module match = registry.getModule(module);
                  
                  if(match != null) {
                     type = match.getType(name); // get imports from the outer module if it exists
                  }
               }
            }
         }
         return type;
      } catch(Exception e){
         throw new InternalStateException("Could not find '" + name + "' in '" + resource + "'", e);
      }
   }

}
