package org.snapscript.compile.instruction.define;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Model;
import org.snapscript.core.Scope;

public class StaticScopeMerger {

   private final Scope inner; // static scope
   
   public StaticScopeMerger(Scope inner) {
      this.inner = inner;
   }
   
   public Scope merge(Scope external) { // do we need to this this always?
      Model model = external.getModel();
      
      if(model != null) {
         return new CompoundScope(model, inner, inner);
      }
      return inner;
   }
}
