package org.snapscript.compile.instruction.define;

import org.snapscript.core.Instance;
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

   public Scope create(Scope scope, Object left) throws Exception {
      Instance instance = (Instance)scope;
      Type real = (Type)left;
      
      if(left == null) {
         throw new InternalArgumentException("Type required for super function call");
      }
      Instance outer = instance.getOuter();
      Scope primitive = instance.getScope();
      Model model = scope.getModel();
      int depth = outer.getDepth();
      
      return new SuperInstance(model, primitive, outer, real, type, depth + 1);
   }
}
