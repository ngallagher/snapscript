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
   
   private final ModuleBuilder builder;
   private final Statement body;
   
   public ModuleDefinition(ModuleName module, Statement... body) {
      this.builder = new ModuleBuilder(module);
      this.body = new ModuleBody(body);
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      Module module = builder.create(scope);
      Value value = ValueType.getTransient(module);
      Scope inner = module.getScope();
      State state = inner.getState();
      
      state.addConstant(TYPE_THIS, value);
      
      return body.execute(inner); // is this a good idea?
   }
}
