package org.snapscript.core.handle;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class HandleBuilder {
   
   private final HandleTypeExtractor extractor;
   
   public HandleBuilder() {
      this.extractor = new HandleTypeExtractor();
   }

   public Object create(Scope scope, Object left, String name) throws Exception {
      if(left != null) {
         Type type = extractor.extract(scope, left);
         
         if(type != null) {
            return new Handle(name, type);
         }
      }
      return name;
   }
}
