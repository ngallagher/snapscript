package org.snapscript.interpret.define;

import org.snapscript.core.Statement;
import org.snapscript.interpret.ArgumentList;
import org.snapscript.interpret.ParameterList;

public class EnumConstructor extends MemberConstructor {

   public EnumConstructor(ModifierList modifier, ParameterList parameters, ArgumentList list, Statement statement) {
      super(modifier, parameters, list, statement);
   }

}
