package org.snapscript.compile.instruction.define;

import org.snapscript.core.Model;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.instance.Instance;
import org.snapscript.core.instance.SuperInstance;

public class SuperInstanceBuilder {
   
   private final Type type;
   
   public SuperInstanceBuilder(Type type) {
      this.type = type;
   }

   public Scope create(Scope scope, Object left) throws Exception {
      Type real = (Type)left;
      Instance instance = (Instance)scope;
      Instance outer = instance.getOuter();
      Module module = type.getModule();
      Model model = scope.getModel();
      
      return new SuperInstance(module, model, outer, type, real);
   }
}
