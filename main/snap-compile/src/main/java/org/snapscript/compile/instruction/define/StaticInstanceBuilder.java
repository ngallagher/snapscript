package org.snapscript.compile.instruction.define;

import org.snapscript.core.CompoundInstance;
import org.snapscript.core.Instance;
import org.snapscript.core.Model;
import org.snapscript.core.PrimitiveInstance;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class StaticInstanceBuilder {
   
   private final StaticPropertyCounter counter;
   private final Scope inner;
   private final Type type;
   
   public StaticInstanceBuilder(Scope inner, Type type) {
      this.counter = new StaticPropertyCounter(type);
      this.inner = inner;
      this.type = type;
   }

   public Instance create(Scope scope, Instance instance) throws Exception {
      Model model = scope.getModel();
      
      if(instance != null) { 
         Scope primitive = instance.getScope();
         int depth = instance.getDepth();
         int count = counter.count();
         
         if(count > 0) {
            return new CompoundInstance(model, primitive, inner, instance, type, depth + 1);
         }
         return instance; // use existing instance, we can ignore inner
      }
      return new PrimitiveInstance(model, inner, type, 0); // create the first instance
   }
}
