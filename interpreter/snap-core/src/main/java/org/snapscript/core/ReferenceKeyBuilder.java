package org.snapscript.core;

public class ReferenceKeyBuilder {
   
   private final ReferenceTypeExtractor extractor;
   
   public ReferenceKeyBuilder() {
      this.extractor = new ReferenceTypeExtractor();
   }

   public Object create(Scope scope, Object left, String name) throws Exception {
      if(left != null) {
         Type type = extractor.extract(scope, left);
         
         if(type != null) {
            return new ReferenceKey(name, type);
         }
      }
      return name;
   }
}
