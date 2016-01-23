package org.snapscript.core;
  
public class PackageManager {
   
   private final ImportScanner scanner;
   private final PackageLoader loader;
   
   public PackageManager(PackageLoader loader, ImportScanner scanner) {
      this.scanner = scanner;
      this.loader = loader;
   }
   
   public Package importPackage(String module) {
      Object result = scanner.importPackage(module);
      
      if(result == null) {
         try {
            return loader.load(module);
         } catch(Exception e){
            throw new IllegalStateException("Problem importing '" + module + "'", e);
         }
      }
      return new NoPackage();
   }
   
   public Package importType(String module, String name) {
      Object result = scanner.importType(module + "." + name);
      
      if(result == null) {
         try {
            return loader.load(module);
         } catch(Exception e){
            throw new IllegalStateException("Problem importing '" + module + "'", e);
         }
      }
      return new NoPackage();
   }
}
