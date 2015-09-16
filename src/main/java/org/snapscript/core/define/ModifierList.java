package org.snapscript.core.define;

import org.snapscript.core.Holder;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.execute.Evaluation;

public class ModifierList implements Evaluation {
   
   private final Modifier[] modifiers;
   
   public ModifierList(Modifier... modifiers){
      this.modifiers = modifiers;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      int mask = 0;
      
      for(Modifier modifier : modifiers) {
         Value value = modifier.evaluate(scope, left);         
         ModifierType type = value.getValue();
         
         if(type != null) {
            if((mask & type.mask) == type.mask) {
               throw new IllegalStateException("Modifier '" + type + "' declared twice");
            }
            mask |= type.mask;
         }
      }
      return new Holder(mask);
   }

}
