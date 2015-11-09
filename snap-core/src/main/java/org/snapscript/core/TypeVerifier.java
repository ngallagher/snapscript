package org.snapscript.core;

import java.util.List;

public class TypeVerifier {
   
   private final PrimitivePromoter converter;
   private final TypeLoader loader;
   
   public TypeVerifier(TypeLoader loader) {
      this.converter = new PrimitivePromoter();
      this.loader = loader;
   }

   public boolean same(Class require, Type type) throws Exception {
      Type actual = loader.load(require);
      
      if(actual == type) {
         return true;
      }
      return false;
   }
   
   public boolean like(Class require, Type type) throws Exception {
      Type actual = loader.load(require);
      List<Type> types = actual.getTypes();
      
      if(actual != type) {
         return types.contains(type);
      }
      return true;
   }
}
