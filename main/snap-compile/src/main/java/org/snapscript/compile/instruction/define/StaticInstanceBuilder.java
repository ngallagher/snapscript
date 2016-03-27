package org.snapscript.compile.instruction.define;

import org.snapscript.core.CompoundInstance;
import org.snapscript.core.Instance;
import org.snapscript.core.Model;
import org.snapscript.core.PrimitiveInstance;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class StaticInstanceBuilder {
   
   private final Scope inner;
   private final Type type;
   
   public StaticInstanceBuilder(Scope inner, Type type) {
      this.inner = inner;
      this.type = type;
   }

   public Instance create(Scope scope, Instance instance) throws Exception {
      Model model = scope.getModel();
      
      if(instance != null) { 
         int depth = instance.getDepth();
         
         return new CompoundInstance(model, inner, instance, depth + 1);
      }
      return new PrimitiveInstance(model, inner, type, 0); // create the first instance
   }
}
