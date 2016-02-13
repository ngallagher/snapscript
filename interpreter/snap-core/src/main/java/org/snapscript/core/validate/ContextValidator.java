package org.snapscript.core.validate;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleBuilder;

public class ContextValidator {

   private final ModuleValidator validator;
   
   public ContextValidator() {
      this.validator = new ModuleValidator();
   }
   
   public void validate(Context context) throws Exception {
      ModuleBuilder builder = context.getBuilder();
      
      for(Module module : builder) {
         validator.validate(module);
      }
   }
}
