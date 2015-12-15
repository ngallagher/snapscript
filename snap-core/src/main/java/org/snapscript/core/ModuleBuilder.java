package org.snapscript.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleBuilder {
   
   private final Map<String, Module> modules;
   private final Context context;
   private final Module module;

   public ModuleBuilder(Context context){
      this.modules = new ConcurrentHashMap<String, Module>();
      this.module = new ContextModule(context);
      this.context = context;
   }
   
   @Bug("Resolve is not the right name here")
   public Module resolve() {
      return module;
   }

   @Bug("Resolve is not the right name here")
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
            Module module = new ContextModule(context, name);
            
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
