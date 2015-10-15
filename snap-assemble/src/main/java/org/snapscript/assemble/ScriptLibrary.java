package org.snapscript.assemble;

import org.snapscript.core.Library;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class ScriptLibrary implements Library {
   
   private final Statement script;

   public ScriptLibrary() {
      this(null);
   }
   
   public ScriptLibrary(Statement script) {
      this.script = script;
   }

   @Override
   public void include(Scope scope) throws Exception {
      if(script != null) {
         script.compile(scope);
         script.execute(scope);
      }
   }

}
