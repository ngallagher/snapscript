package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class TraitFunction extends MemberFunction {
 
   public TraitFunction(AnnotationList annotations, ModifierList list, Evaluation identifier, ParameterList parameters){
      super(annotations, list, identifier, parameters);
   }
   
   public TraitFunction(AnnotationList annotations, ModifierList list, Evaluation identifier, ParameterList parameters, Constraint constraint){
      super(annotations, list, identifier, parameters, constraint);
   }
   
   public TraitFunction(AnnotationList annotations, ModifierList list, Evaluation identifier, ParameterList parameters, Statement body){  
      super(annotations, list, identifier, parameters, body);
   }
   
   public TraitFunction(AnnotationList annotations, ModifierList list, Evaluation identifier, ParameterList parameters, Constraint constraint, Statement body){  
      super(annotations, list, identifier, parameters, constraint, body);
   } 
   
   @Override
   protected Initializer define(Initializer initializer, Type type, int mask) throws Exception {
      if(body == null) {
         return super.define(initializer, type, ModifierType.ABSTRACT.mask);
      }  
      return super.define(initializer, type, 0);
   }
}
