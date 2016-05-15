package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.AnnotationList;

public class TraitDefinition extends ClassDefinition {
   
   public TraitDefinition(AnnotationList annotations, TraitName name, TypeHierarchy hierarchy, TypePart... parts) {
      super(annotations, name, hierarchy, parts);     
   }

}
