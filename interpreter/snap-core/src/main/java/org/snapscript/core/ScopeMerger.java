package org.snapscript.core;

public class ScopeMerger {

   private final Context context;
   private final String name;
   
   public ScopeMerger(Context context, String name) {
      this.context = context;
      this.name = name;
   }
   
   public Scope merge(Model model) {
      ModuleBuilder builder = context.getBuilder();
      Module module = builder.create(name);
      
      if(module == null) {
         throw new IllegalStateException("Module '" +name +"' not found");
      }
      return new ModelScope(model, module);
   }
}
