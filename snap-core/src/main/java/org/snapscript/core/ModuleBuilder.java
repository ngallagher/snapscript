package org.snapscript.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleBuilder {
   
   private final Map<String, Module> modules;
   private final Context context;
   private final Module module;
   private final Model model;

   public ModuleBuilder(Context context, Model model){
      this.modules = new ConcurrentHashMap<String, Module>();
      this.module = new ContextModule(context, model);
      this.context = context;
      this.model = model;
   }
   
   public Module resolve() {
      return module;
   }

   public Module resolve(String name) {
      if(name != null) {
         return modules.get(name);
      }
      return module;
   }
   
   public Module create(String name) {
      if(name != null) {
         int length = name.length();
         
         if(length == 0) {
            return module;
         }
         Module current = modules.get(name);
         
         if(current == null) {
            Module module = new ContextModule(context, model, name);
            
            if(name != null) {           
               modules.put(name, module);
            }
            return module;
         }
         return current;
      }
      return module;
   }
}
