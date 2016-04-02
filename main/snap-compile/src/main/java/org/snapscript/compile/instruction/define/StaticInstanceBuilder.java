package org.snapscript.compile.instruction.define;

import org.snapscript.core.Model;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.instance.Instance;
import org.snapscript.core.instance.PrimitiveInstance;
import org.snapscript.core.instance.StaticInstance;

public class StaticInstanceBuilder {
   
   private final Scope inner;
   private final Type type;
   
   public StaticInstanceBuilder(Scope inner, Type type) {
      this.inner = inner;
      this.type = type;
   }

   public Instance create(Scope scope, Instance base, Type real) throws Exception {
      Module module = type.getModule();
      Model model = scope.getModel();
      
      if(base != null) { 
         return new StaticInstance(module, model, inner, base, real);
      }
      return new PrimitiveInstance(module, model, inner, real); // create the first instance
   }
}
