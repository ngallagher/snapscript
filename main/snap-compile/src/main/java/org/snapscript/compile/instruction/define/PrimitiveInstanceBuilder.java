package org.snapscript.compile.instruction.define;

import org.snapscript.core.Model;
import org.snapscript.core.PrimitiveInstance;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class PrimitiveInstanceBuilder {
   
   private final Type type;
   
   public PrimitiveInstanceBuilder(Type type) {
      this.type = type;
   }

   public Scope create(Scope scope) throws Exception {
      Scope instance = scope.getInner(); 
      Model model = instance.getModel();
      
      return new PrimitiveInstance(model, instance, type);
   }
}
