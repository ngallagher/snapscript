package org.snapscript.code.define;

import org.snapscript.code.ModifierList;
import org.snapscript.code.annotation.AnnotationList;
import org.snapscript.code.function.ParameterList;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.define.Initializer;

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