package org.snapscript.compile.instruction.define;

import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.SuperExtractor;
import org.snapscript.core.Type;

public class DelegateInitializer {

   private final SuperConstructor constructor;
   private final SuperExtractor extractor;
   private final TypePart part;

   public DelegateInitializer(TypePart part){  
      this.constructor = new SuperConstructor();
      this.extractor = new SuperExtractor();
      this.part = part;
   } 

   public Initializer define(Scope scope, Type type) throws Exception {
      Type base = extractor.extractor(type);
      
      if(part != null){
         return part.define(scope, null, type);              
      }
      if(base != null) {
         return constructor.define(scope, null, type);
      }
      return new PrimitiveConstructor(); 
   }
}
