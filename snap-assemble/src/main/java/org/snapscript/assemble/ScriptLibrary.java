package org.snapscript.assemble;

import org.snapscript.core.Library;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class ScriptLibrary implements Library {
   
   private final Statement script;
   private final String name;
   
   public ScriptLibrary(Statement script, String name) {
      this.script = script;
      this.name = name;
   }

   @Override
   public void include(Scope scope) throws Exception {
      try {
         script.compile(scope);
         script.execute(scope);
      } catch(Exception e) {
         throw new IllegalStateException("Error occured in '" + name + "'");
      }
   }

}
