package org.snapscript.core;

public class TypeVerifier {
   
   private final InstanceChecker checker;
   private final TypeLoader loader;
   
   public TypeVerifier(TypeLoader loader, InstanceChecker checker) {
      this.checker = checker;
      this.loader = loader;
   }

   public boolean same(Class require, Type type) throws Exception {
      Type actual = loader.loadType(require);
      
      if(actual == type) {
         return true;
      }
      return false;
   }
   
   public boolean like(Class require, Type type) throws Exception {
      Type actual = loader.loadType(require);
      
      if(checker.check(actual, type)) {
         return true;
      }
      return false;
   }
}
