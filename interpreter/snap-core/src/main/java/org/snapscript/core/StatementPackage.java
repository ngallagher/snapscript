package org.snapscript.core;

import java.util.concurrent.atomic.AtomicBoolean;

public class StatementPackage implements Package {
   
   private final AtomicBoolean compile;
   private final Statement statement;
   private final String name;
   
   public StatementPackage(Statement statement, String name) {
      this.compile = new AtomicBoolean(true);
      this.statement = statement;
      this.name = name;
   }

   @Override
   public Statement compile(Scope scope) throws Exception {
      if(compile.compareAndSet(true, false)) {
         Module module = scope.getModule();
         Context context = module.getContext();
         
         try {
            ModuleRegistry registry = context.getRegistry();
            Module library = registry.addModule(name);
            Scope inner = library.getScope();
           
            statement.compile(inner); 
         } catch(Exception e) {
            if(name != null) {
               throw new InternalStateException("Error occured in '" + name + "'", e);
            }
            throw new InternalStateException("Error occured in script", e);
         }
      }
      return statement;
   }

}
