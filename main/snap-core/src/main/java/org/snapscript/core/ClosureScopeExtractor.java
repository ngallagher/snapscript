package org.snapscript.core;

import java.util.Set;

public class ClosureScopeExtractor {

   public ClosureScopeExtractor() {
      super();
   }
   
   public Scope extract(Scope scope) {
      Model model = scope.getModel();
      State state = scope.getState();
      Set<String> names = state.getNames();
      
      if(!names.isEmpty()) {
         Scope capture = new ClosureScope(model, scope);
         
         for(String name : names) {
            State inner = capture.getState();
            inner.getValue(name);
         }
         return capture;
      }
      return scope;
   }
}
