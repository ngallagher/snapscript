package org.snapscript.core;

public class TypeExtractor {
   
   private final TypeLoader loader;
   
   public TypeExtractor(TypeLoader loader) {
      this.loader = loader;
   }
   
   public Type extract(Object value) throws Exception {
      if(value != null) {
         Class type = value.getClass();
         
         if(SuperScope.class.isAssignableFrom(type)) {
            SuperScope scope = (SuperScope)value;
            return scope.getSuper();
         }
         if(Scope.class.isAssignableFrom(type)) {
            Scope scope = (Scope)value;
            return scope.getType();
         }
         return loader.load(type);
      }
      return null;
   }
}
