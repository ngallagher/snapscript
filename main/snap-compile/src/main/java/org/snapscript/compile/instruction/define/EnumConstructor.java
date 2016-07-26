package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Initializer;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class EnumConstructor extends ClassConstructor {

   public EnumConstructor(AnnotationList annotations, ModifierList modifiers, ParameterList parameters, Statement body) {
      super(annotations, modifiers, parameters, body);
   }

   @Override
   public Initializer compile(Initializer statements, Type type) throws Exception {
      return compile(statements, type, false);
   }
}
