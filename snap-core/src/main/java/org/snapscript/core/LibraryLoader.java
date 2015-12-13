package org.snapscript.core;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.snapscript.core.resource.ResourceReader;

public class LibraryLoader {
   
   private static final String EXTENSION = ".snap";
   
   private final ResourceReader reader;
   private final PackageLinker linker;
   private final String suffix;
   private final Set libraries;

   public LibraryLoader(PackageLinker linker, ResourceReader reader){
      this(linker, reader, EXTENSION);
   }
   
   public LibraryLoader(PackageLinker linker, ResourceReader reader, String suffix){
      this.libraries = new CopyOnWriteArraySet();
      this.reader = reader;
      this.linker = linker;
      this.suffix = suffix;
   }

   public Package load(String qualifier) throws Exception {
      if(!libraries.contains(qualifier)) {
         String path = qualifier.replace('.', '/');
         
         try {
            String source = reader.read(path + suffix);
            
            try {
               return linker.link(qualifier, source);
            } catch(Exception e) {
               throw new IllegalStateException("Could not load library '" + path + suffix + "'", e);
            }
         } finally {
            libraries.add(qualifier);         
         }
      }
      return new NoLibrary();
   }
}
