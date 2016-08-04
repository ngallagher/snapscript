package org.snapscript.compile;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.ModifierType;

public class ModifierList {
   
   private final Modifier[] modifiers;
   
   public ModifierList(Modifier... modifiers){
      this.modifiers = modifiers;
   }

   public int getModifiers() {
      int mask = 0;
      
      for(Modifier modifier : modifiers) {        
         ModifierType type = modifier.getType();
         
         if(type != null) {
            if((mask & type.mask) == type.mask) {
               throw new InternalStateException("Modifier '" + type + "' declared twice");
            }
            mask |= type.mask;
         }
      }
      return mask;
   }

}
