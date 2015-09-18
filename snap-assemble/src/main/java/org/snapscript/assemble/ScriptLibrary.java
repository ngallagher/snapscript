package org.snapscript.assemble;

import org.snapscript.core.Library;
import org.snapscript.core.Scope;


public class ScriptLibrary implements Library {
   
   private final Script script;

   public ScriptLibrary() {
      this(null);
   }
   
   public ScriptLibrary(Script script) {
      this.script = script;
   }

   @Override
   public void include(Scope scope) throws Exception {
      if(script != null) {
         script.execute(scope);
      }
   }

}
