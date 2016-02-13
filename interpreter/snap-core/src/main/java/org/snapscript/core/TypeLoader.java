package org.snapscript.core;

import org.snapscript.core.index.TypeIndexer;

public class TypeLoader {
   
   private final PackageManager manager;
   private final PackageLoader loader;
   private final ImportScanner scanner;
   private final TypeIndexer indexer;
   
   public TypeLoader(PackageLinker linker, ModuleRegistry registry, ResourceManager manager){
      this.scanner = new ImportScanner();
      this.indexer = new TypeIndexer(registry, scanner);
      this.loader = new PackageLoader(linker, manager);
      this.manager = new PackageManager(loader, scanner);
   }
   
   public synchronized Package importPackage(String module) throws Exception  {
      return manager.importPackage(module);
   }   
   
   public synchronized Package importType(String module, String name) throws Exception {
      return manager.importType(module, name); 
   }
   
   public synchronized Type resolveType(String module, String name) throws Exception {
      return indexer.loadType(module, name);
   }
   
   public synchronized Type defineType(String module, String name) throws Exception {
      return indexer.defineType(module, name);
   }
   
   public synchronized Type loadType(Class type) throws Exception {
      return indexer.loadType(type);
   } 
}
