package org.snapscript.compile.instruction.define;

import org.snapscript.core.Instance;
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
      Instance outer = instance.getOuter();
      Model model = scope.getModel();
      int depth = outer.getDepth();
      
      return new SuperInstance(model, outer, type, depth + 1);
   }
}
