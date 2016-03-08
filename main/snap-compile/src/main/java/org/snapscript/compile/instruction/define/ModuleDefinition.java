package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_THIS;

import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ModuleDefinition extends Statement {   
   
   private final AtomicReference<Module> reference;
   private final ModuleBuilder builder;
   private final Statement body;
   
   public ModuleDefinition(ModuleName module, Statement... body) {
      this.reference = new AtomicReference<Module>();
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
      reference.set(module);
      
      return body.compile(inner); 
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Module module = reference.get();
      Scope inner = module.getScope();
      
      return body.execute(inner); // requires order for use!
   }
}
