package org.snapscript.core;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

import org.snapscript.common.ThreadPool;

public class PackageLoader {
   
   private final PathConverter converter;
   private final ResourceManager manager;
   private final PackageLinker linker;
   private final Executor executor;
   private final Set libraries;

   public PackageLoader(PackageLinker linker, ResourceManager manager){
      this(linker, manager, 8);
   }
   
   public PackageLoader(PackageLinker linker, ResourceManager manager, int threads){
      this.executor = new ThreadPool(threads);
      this.linker = new ExecutorLinker(linker, executor);
      this.libraries = new CopyOnWriteArraySet();
      this.converter = new PathConverter();
      this.manager = manager;
   }

   public Package load(String qualifier) throws Exception {
      if(libraries.add(qualifier)) { // load only once!
         String path = converter.createPath(qualifier);
         String source = manager.getString(path);
         
         try {
            return linker.link(qualifier, source);
         } catch(Exception e) {
            throw new InternalStateException("Could not load library '" + path + "'", e);
         }
      }
      return new NoPackage();
   }
}
