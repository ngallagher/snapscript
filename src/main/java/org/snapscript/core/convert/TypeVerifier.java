package org.snapscript.core.convert;

import java.util.List;

import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class TypeVerifier {
   
   private final TypeLoader loader;
   
   public TypeVerifier(TypeLoader loader) {
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
