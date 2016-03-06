package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class TraitFunction extends TypeFunction {
 
   public TraitFunction(ModifierList list, Evaluation identifier, ParameterList parameters) {
      super(list, identifier, parameters, null);
   }
   
   public TraitFunction(ModifierList list, Evaluation identifier, ParameterList parameters, Statement body) {
      super(list, identifier, parameters, body);
   }
   
   @Override
   protected Initializer define(Scope scope, Initializer initializer, Type type, int mask) throws Exception {
      if(body == null) {
         return super.define(scope, initializer, type, ModifierType.ABSTRACT.mask);
      }  
      return super.define(scope, initializer, type, 0);
   }
}
