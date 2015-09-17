package org.snapscript.core;


public class TypeExtractor {
   
   private final TypeLoader loader;
   
   public TypeExtractor(TypeLoader loader) {
      this.loader = loader;
   }
   
   public Type extract(Object value) throws Exception {
      if(value != null) {
         Class type = value.getClass();
         
         if(Scope.class.isInstance(value)) {
            Scope scope = (Scope)value;
            return scope.getType();
         }
         return loader.load(type);
      }
      return null;
   }
}
