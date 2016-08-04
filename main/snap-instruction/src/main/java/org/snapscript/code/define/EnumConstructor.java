package org.snapscript.code.define;

import org.snapscript.code.ModifierList;
import org.snapscript.code.annotation.AnnotationList;
import org.snapscript.code.function.ParameterList;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.define.Initializer;

public class EnumConstructor extends ClassConstructor {

   public EnumConstructor(AnnotationList annotations, ModifierList modifiers, ParameterList parameters, Statement body) {
      super(annotations, modifiers, parameters, body);
   }

   @Override
   public Initializer compile(Initializer statements, Type type) throws Exception {
      return compile(statements, type, false);
   }
}
