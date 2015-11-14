package org.snapscript.core;

public class TypeLoader {
   
   private final TypeIndexer indexer;
   
   public TypeLoader(ImportStore store, ImportResolver resolver){
      this.indexer = new TypeIndexer(store, resolver);
   }
   
   public Library importPackage(String name) {
      return indexer.addImport(name);
   }   
   
   public Library importType(String location, String name) {
      return indexer.addType(location, name);
   }
   
   public Type resolveType(String name, String module) throws Exception {
      return indexer.load(name, module, false);
   }
   
   public Type defineType(String name, String module) throws Exception {
      return indexer.load(name, module, true);
   }
   
   public Type loadType(Class cls) throws Exception {
      return indexer.load(cls);
   } 
}
