package org.snapscript.core;

public class ExecutableLibrary implements Executable{
   
   private final Package library;
   private final Context context;
 
   public ExecutableLibrary(Context context, Package library){
      this.library = library;
      this.context = context;
   }
   
   @Override
   public void execute() throws Exception{ 
      ModuleBuilder builder = context.getBuilder();
      Module module = builder.resolve();
      Scope scope = module.getScope();

      library.include(scope); // this can only be executed one for 'null' the default module, we need to reset it
   }

}
