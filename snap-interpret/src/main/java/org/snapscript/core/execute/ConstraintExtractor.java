package org.snapscript.core.execute;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class ConstraintExtractor {
   
   public ConstraintExtractor() {
      super();
   }

   public Type extract(Scope scope, Object value) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      TypeLoader loader = context.getLoader();
      Class type = value.getClass();
      
      if(Scope.class.isInstance(value)) {
         Scope definition = (Scope)value;
         return definition.getType();
      }
      return loader.load(type);
   }
}
