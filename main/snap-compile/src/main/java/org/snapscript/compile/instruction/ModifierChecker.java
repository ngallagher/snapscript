package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.define.MemberDeclaration;
import org.snapscript.core.ModifierType;

public class ModifierChecker {

   private MemberDeclaration list;
   private int modifiers;
   
   public ModifierChecker(MemberDeclaration list) {
      this.modifiers = -1;
      this.list = list;
   }
   
   public boolean isStatic() {
      if(modifiers == -1) {
         modifiers = list.getModifiers();
      }
      return ModifierType.isStatic(modifiers);
   }
   
   public boolean isConstant() {
      if(modifiers == -1) {
         modifiers = list.getModifiers();
      }
      return ModifierType.isConstant(modifiers);
   }
   
   public boolean isPublic() {
      if(modifiers == -1) {
         modifiers = list.getModifiers();
      }
      return ModifierType.isPublic(modifiers);
   }
   
   public boolean isPrivate() {
      if(modifiers == -1) {
         modifiers = list.getModifiers();
      }
      return ModifierType.isPrivate(modifiers);
   }
   
   public boolean isOverride() {
      if(modifiers == -1) {
         modifiers = list.getModifiers();
      }
      return ModifierType.isOverride(modifiers);
   }
}
