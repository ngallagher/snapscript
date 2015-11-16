package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Statement;

public class EnumConstructor extends MemberConstructor {

   public EnumConstructor(ModifierList modifier, ParameterList parameters, ArgumentList list, Statement statement) {
      super(modifier, parameters, list, statement);
   }

}
