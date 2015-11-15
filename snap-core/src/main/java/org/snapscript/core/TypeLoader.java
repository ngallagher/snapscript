package org.snapscript.core;

public class TypeLoader {
   
   private final TypeIndexer indexer;
   
   public TypeLoader(ImportResolver resolver){
      this.indexer = new TypeIndexer(resolver);
   }
   
   public synchronized Library importPackage(String name) {
      return indexer.addImport(name);
   }   
   
   public synchronized Library importType(String name, String module) {
      return indexer.addType(name, module); 
   }
   
   public synchronized Type resolveType(String name, String module) throws Exception {
      return indexer.load(name, module, false);
   }
   
   public synchronized Type defineType(String name, String module) throws Exception {
      return indexer.load(name, module, true);
   }
   
   public synchronized Type loadType(Class cls) throws Exception {
      return indexer.load(cls);
   } 
}
