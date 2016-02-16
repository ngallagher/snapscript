package org.snapscript.core;

public class ScopeMerger {

   private final Context context;
   
   public ScopeMerger(Context context) {
      this.context = context;
   }
   
   public Scope merge(Model model, String name) {
      ModuleRegistry registry = context.getRegistry();
      Module module = registry.addModule(name);
      
      if(module == null) {
         throw new InternalStateException("Module '" +name +"' not found");
      }
      return new ModelScope(model, module);
   }
}
