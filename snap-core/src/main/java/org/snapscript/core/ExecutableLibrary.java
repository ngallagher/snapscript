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
      Module module = context.addModule("");
      Scope scope = module.getScope();
      
      library.include(scope);
   }

}
