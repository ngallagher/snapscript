package org.snapscript.compile.instruction.define;

import org.snapscript.core.InternalArgumentException;
import org.snapscript.core.Model;
import org.snapscript.core.Scope;
import org.snapscript.core.SuperInstance;
import org.snapscript.core.Type;

public class SuperInstanceBuilder {
   
   private final Type type;
   
   public SuperInstanceBuilder(Type type) {
      this.type = type;
   }

   public Scope create(Scope instance, Object left) throws Exception {
      Model model = instance.getModel();
      Type real = (Type)left;
      
      if(left == null) {
         throw new InternalArgumentException("Type required for super function call");
      }
      return new SuperInstance(model, instance, real, type);
   }
}
