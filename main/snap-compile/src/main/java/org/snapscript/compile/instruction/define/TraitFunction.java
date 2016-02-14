package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Statement;

public class TraitFunction extends TypeFunction {
 
   public TraitFunction(ModifierList list, Evaluation identifier, ParameterList parameters) {
      super(list, identifier, parameters, null);
   }
   
   public TraitFunction(ModifierList list, Evaluation identifier, ParameterList parameters, Statement body) {
      super(list, identifier, parameters, body);
   }
}
