package org.snapscript.interpret.define;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class ModuleDefinition extends Statement {   
   
   private final ModuleName module;
   private final Statement body;
   
   public ModuleDefinition(ModuleName module, Statement... body) {
      this.body = new ModuleBody(body);
      this.module = module;     
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      Value value = module.evaluate(scope, null);
      String name = value.getString();
      Module module = scope.getModule();
      Context context = module.getContext();
      Module define = context.addModule(name);
      Scope inner = define.getScope();
      
      // this will define the classes in this module!!
      return body.execute(inner);
   }

}
