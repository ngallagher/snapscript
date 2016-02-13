package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class EnumConstructor extends MemberConstructor {

   public EnumConstructor(ModifierList modifier, ParameterList parameters, Statement statement) {
      super(modifier, parameters, statement);
   }

   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      return define(scope, statements, type, true);
   }
}
