package org.snapscript.core.validate;

import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleRegistry;
import org.snapscript.core.convert.ConstraintMatcher;

public class ExecutableValidator {

   private final ModuleValidator validator;
   
   public ExecutableValidator(ConstraintMatcher matcher) {
      this.validator = new ModuleValidator(matcher);
   }
   
   public void validate(Context context) throws Exception {
      ModuleRegistry registry = context.getRegistry();
      List<Module> modules = registry.getModules();
      
      for(Module module : modules) {
         validator.validate(module);
      }
   }
}
