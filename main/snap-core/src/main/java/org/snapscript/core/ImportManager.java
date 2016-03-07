package org.snapscript.core;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ImportManager {
   
   private final Set<String> imports;
   private final Context context;
   private final String prefix;
   
   public ImportManager(Context context, String prefix) {
      this.imports = new CopyOnWriteArraySet<String>();
      this.context = context;
      this.prefix = prefix;
   }
   
   public void addImport(String name) {
      imports.add(name);
   }
   
   public Module getModule(String name) {
      try {
         ModuleRegistry registry = context.getRegistry();
         
         for(String module : imports) {
            Module match = registry.getModule(module + "." + name); // get imports from the outer module if it exists
            
            if(match != null) {
               return match;
            }
         }
         return null;
      } catch(Exception e){
         throw new InternalStateException("Could not find '" + name + "' in '" + prefix + "'", e);
      }
   }

   public Type getType(String name) {
      try {
         TypeLoader loader = context.getLoader();
         Type type = loader.resolveType(prefix, name);
         
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

                     if(type != null) {
                        return type;
                     }
                  }
               }
            }
         }
         return type;
      } catch(Exception e){
         throw new InternalStateException("Could not find '" + name + "' in '" + prefix + "'", e);
      }
   }

}
