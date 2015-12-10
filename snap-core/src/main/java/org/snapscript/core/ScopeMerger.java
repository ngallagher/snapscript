package org.snapscript.core;

public class ScopeMerger {

   private final Context context;
   
   public ScopeMerger(Context context) {
      this.context = context;
   }
   
   public Scope merge(Model model) {
      ModuleBuilder builder = context.getBuilder();
      Module module = builder.resolve();
      
      return new ModelScope(model, module);
   }
}
