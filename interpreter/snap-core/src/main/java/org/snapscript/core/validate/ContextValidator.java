package org.snapscript.core.validate;

import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleRegistry;

public class ContextValidator {

   private final ModuleValidator validator;
   
   public ContextValidator() {
      this.validator = new ModuleValidator();
   }
   
   public void validate(Context context) throws Exception {
      ModuleRegistry registry = context.getRegistry();
      List<Module> modules = registry.getModules();
      
      for(Module module : modules) {
         validator.validate(module);
      }
   }
}
