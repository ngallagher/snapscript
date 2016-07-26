package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Initializer;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ClassConstructor extends MemberConstructor {
   
   public ClassConstructor(AnnotationList annotations, ModifierList modifiers, ParameterList parameters, Statement body){  
      super(annotations, modifiers, parameters, null, body);
   }  
   
   public ClassConstructor(AnnotationList annotations, ModifierList modifiers, ParameterList parameters, TypePart part, Statement body){  
      super(annotations, modifiers, parameters, part, body);
   } 
   
   @Override
   public Initializer compile(Initializer initializer, Type type) throws Exception {
      return compile(initializer, type, true);
   }
}