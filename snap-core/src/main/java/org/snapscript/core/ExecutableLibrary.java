package org.snapscript.core;

public class ExecutableLibrary implements Executable{
   
   private final Library library;
   private final Context context;
 
   public ExecutableLibrary(Context context, Library library){
      this.library = library;
      this.context = context;
   }
   
   @Override
   public void execute(Model model) throws Exception{ 
      Module module = context.getModule();
      Scope base = new ModuleScope(module);
      Scope scope = new ModelScope(base, model);
      
      library.include(scope);
   }

}
