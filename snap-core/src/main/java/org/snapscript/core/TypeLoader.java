package org.snapscript.core;

import org.snapscript.core.index.TypeIndexer;

public class TypeLoader {
   
   private final TypeIndexer indexer;
   
   public TypeLoader(ImportResolver resolver, ModuleBuilder builder){
      this.indexer = new TypeIndexer(resolver, builder);
   }
   
   public synchronized Package importPackage(String module) {
      return indexer.addImport(module);
   }   
   
   public synchronized Package importType(String module, String name) {
      return indexer.addImport(module, name); 
   }
   
   public synchronized Type resolveType(String module, String name) throws Exception {
      return indexer.load(module, name, false);
   }
   
   public synchronized Type defineType(String module, String name) throws Exception {
      return indexer.load(module, name, true);
   }
   
   public synchronized Type loadType(Class cls) throws Exception {
      return indexer.load(cls);
   } 
}
