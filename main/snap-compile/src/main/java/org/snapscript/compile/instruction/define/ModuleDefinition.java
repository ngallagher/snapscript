package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_THIS;

import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ModuleDefinition extends Statement {   
   
   private final ModuleName module;
   private final Statement body;
   
   public ModuleDefinition(ModuleName module, Statement... body) {
      this.body = new ModuleBody(body);
      this.module = module;     
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      Value value = create(scope);
      Module module = value.getValue();
      Scope inner = module.getScope();
      State state = inner.getState();
      
      state.addConstant(TYPE_THIS, value);
      
      return body.execute(inner); // is this executing stuff
   }
   
   protected Value create(Scope scope) throws Exception {
      Value value = module.evaluate(scope, null);
      String name = value.getString();
      Module parent = scope.getModule();
      String prefix = parent.getName();
      Module module = parent.addModule(prefix,  name);
      String include = parent.getName();
          
      parent.addModule(prefix,  name); // make module accessible by name
      module.addModule(include); // make outer classes accessible
      
      return ValueType.getConstant(module);
   }

}
