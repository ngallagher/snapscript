package org.snapscript.core.validate;

import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.Reserved.TYPE_SUPER;
import static org.snapscript.core.Reserved.TYPE_THIS;

import org.snapscript.core.Model;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class InstanceValidator {
   
   private static final String[] CONSTANTS = { TYPE_THIS, TYPE_SUPER, TYPE_CLASS };
   
   private final TypeValidator validator;
   
   public InstanceValidator() {
      this.validator = new TypeValidator();
   }
   
   public void validateInstance(Scope instance) throws Exception {
      State state = instance.getState();
      Type type = instance.getType();
      Model model = instance.getModel();
      
      if(model == null) {
         throw new IllegalStateException("Instance of '" + type+ "' does not reference model");
      }
      for(String constant : CONSTANTS) {
         Value value = state.getValue(constant);
         
         if(value == null) {
            throw new IllegalStateException("Constant '" + constant + "' not defined for '" + type+ "'");
         }
         Object object = value.getValue();
         
         if(object == null) {
            throw new IllegalStateException("Constant '" + constant + "' not set for '" + type+ "'");
         }
      }
      validator.validate(type);
   }

}
