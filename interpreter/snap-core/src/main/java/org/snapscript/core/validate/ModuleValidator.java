package org.snapscript.core.validate;

import java.util.List;

import org.snapscript.core.Module;
import org.snapscript.core.Type;

public class ModuleValidator {

   private final TypeValidator validator;
   
   public ModuleValidator() {
      this.validator = new TypeValidator();
   }
   
   public void validate(Module module) throws Exception {
      List<Type> types = module.getTypes();
      String resource = module.getName();
      
      for(Type type : types) {
         Module parent = type.getModule();
         String prefix = parent.getName();
         String name = type.getName();

         try {
            validator.validate(type);
         }catch(Exception e) {
            throw new IllegalStateException("Invalid reference to '" + prefix + "." + name +"' in '" + resource + "'", e);
         }
      }
   }
}
