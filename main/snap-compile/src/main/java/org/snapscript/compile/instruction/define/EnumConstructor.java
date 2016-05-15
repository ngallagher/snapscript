package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class EnumConstructor extends ClassConstructor {

   public EnumConstructor(MemberDeclaration modifier, ParameterList parameters, Statement body) {
      super(modifier, parameters, body);
   }

   @Override
   public Initializer compile(Scope scope, Initializer statements, Type type) throws Exception {
      return compile(scope, statements, type, false);
   }
}
