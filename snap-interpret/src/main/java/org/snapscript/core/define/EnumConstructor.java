package org.snapscript.core.define;

import org.snapscript.core.Statement;
import org.snapscript.core.execute.ArgumentList;
import org.snapscript.core.execute.ParameterList;

public class EnumConstructor extends MemberConstructor {

   public EnumConstructor(ModifierList modifier, ParameterList parameters, ArgumentList list, Statement statement) {
      super(modifier, parameters, list, statement);
   }

}
