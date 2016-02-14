package org.snapscript.compile.instruction.define;

import org.snapscript.core.InternalArgumentException;
import org.snapscript.core.Model;
import org.snapscript.core.Scope;
import org.snapscript.core.SuperScope;
import org.snapscript.core.Type;

public class SuperScopeBuilder {
   
   private final Type type;
   
   public SuperScopeBuilder(Type type) {
      this.type = type;
   }

   public Scope create(Scope instance, Object left) throws Exception {
      Model model = instance.getModel();
      Type real = (Type)left;
      
      if(left == null) {
         throw new InternalArgumentException("Type required for super function call");
      }
      return new SuperScope(model, instance, real, type);
   }
}
