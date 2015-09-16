package org.snapscript.core.execute;

import org.snapscript.Executable;
import org.snapscript.Model;
import org.snapscript.core.Context;
import org.snapscript.core.ModelScope;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleScope;
import org.snapscript.core.Scope;

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
