package org.snapscript.core;

import static org.snapscript.core.Reserved.SCRIPT_EXTENSION;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PackageLoader {
   
   private final ResourceManager manager;
   private final PackageLinker linker;
   private final String suffix;
   private final Set libraries;

   public PackageLoader(PackageLinker linker, ResourceManager manager){
      this(linker, manager, SCRIPT_EXTENSION);
   }
   
   public PackageLoader(PackageLinker linker, ResourceManager manager, String suffix){
      this.libraries = new CopyOnWriteArraySet();
      this.manager = manager;
      this.linker = linker;
      this.suffix = suffix;
   }

   public Package load(String qualifier) throws Exception {
      if(!libraries.contains(qualifier)) {
         String path = qualifier.replace('.', '/');
         
         try {
            String source = manager.getString(path + suffix);
            
            try {
               return linker.link(qualifier, source);
            } catch(Exception e) {
               throw new IllegalStateException("Could not load library '" + path + suffix + "'", e);
            }
         } finally {
            libraries.add(qualifier);         
         }
      }
      return new NoPackage();
   }
}
