package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.core.Bug;

@Bug("This is rubbish? - maybe we should get rid of this?")
public class MemberDeclaration {
   
   private final AnnotationList annotations;
   private final ModifierList modifiers;

   public MemberDeclaration() {
      this(null, null);
   }
   
   public MemberDeclaration(ModifierList modifiers) {
      this(null, modifiers);
   }
   
   public MemberDeclaration(AnnotationList annotations) {
      this(annotations, null);
   }
   
   public MemberDeclaration(AnnotationList annotations, ModifierList modifiers) {
      this.annotations = annotations;
      this.modifiers = modifiers;
   }

   public int getModifiers() {
      if(modifiers != null) {
         return modifiers.getModifiers();
      }
      return 0;
   }
}
