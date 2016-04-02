package org.snapscript.core;

import static org.snapscript.core.convert.ConstraintConverter.INVALID;

public class TypeVerifier {
   
   private final TypeCastChecker checker;
   private final TypeLoader loader;
   
   public TypeVerifier(TypeLoader loader, TypeCastChecker checker) {
      this.checker = checker;
      this.loader = loader;
   }

   public boolean isSame(Type type, Class require) throws Exception {
      Type actual = loader.loadType(require);
      
      if(actual == type) {
         return true;
      }
      return false;
   }
   
   public boolean isLike(Type type, Class require) throws Exception {
      Type actual = loader.loadType(require);
      int score = checker.cast(type, actual);
      
      return score > INVALID;
   }
   
   public boolean isArray(Type type) throws Exception {
      Class real = type.getType();
      
      if(real != null) {
         return real.isArray();
      }
      return false;
   }
}
