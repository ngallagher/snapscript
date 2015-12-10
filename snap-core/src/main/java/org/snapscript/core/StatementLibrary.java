package org.snapscript.core;

import java.util.concurrent.atomic.AtomicBoolean;

public class StatementLibrary implements Package {
   
   private final AtomicBoolean done;
   private final Statement script;
   private final String name;
   
   public StatementLibrary(Statement script, String name) {
      this.done = new AtomicBoolean();
      this.script = script;
      this.name = name;
   }

   @Override
   public Statement compile(Scope scope) throws Exception {
      if(done.compareAndSet(false, true)) {
         Module module = scope.getModule();
         Context context = module.getContext();
         
         try {
            ModuleBuilder builder = context.getBuilder();
            Module library = builder.create(name); // create a new named module
            Scope inner = library.getScope();
           
            script.compile(inner); // compile it with a different module, all will go in to context
         } catch(Exception e) {
            if(name != null) {
               throw new IllegalStateException("Error occured in '" + name + "'", e);
            }
            throw new IllegalStateException("Error occured in script", e);
         }
      }
      return script;
   }

}
