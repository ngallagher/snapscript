package org.snapscript.core;

import java.util.concurrent.atomic.AtomicBoolean;

public class StatementLibrary implements Library {
   
   private final AtomicBoolean done;
   private final Statement script;
   private final String name;
   
   public StatementLibrary(Statement script, String name) {
      this.done = new AtomicBoolean();
      this.script = script;
      this.name = name;
   }

   @Override
   public void include(Scope scope) throws Exception {
      if(done.compareAndSet(false, true)) {
         Module module = scope.getModule();
         Context context = module.getContext();
         
         try {
            ModuleBuilder builder = context.getBuilder();
            Module inner = builder.create(name); // create a new named module
            Scope scp = inner.getScope();
           
            script.compile(scp); // compile it with a different module, all will go in to context
            script.execute(scp);
         } catch(Exception e) {
            if(name != null) {
               throw new IllegalStateException("Error occured in '" + name + "'", e);
            }
            throw new IllegalStateException("Error occured in script", e);
         }
      }
   }

}
