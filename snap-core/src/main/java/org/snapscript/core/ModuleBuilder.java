package org.snapscript.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.core.export.SystemExporter;

public class ModuleBuilder {

   private final Map<String, Module> modules;
   private final SystemExporter exporter;
   private final Context context;

   public ModuleBuilder(Context context) {
      this.modules = new ConcurrentHashMap<String, Module>();
      this.exporter = new SystemExporter(context);
      this.context = context;
   }

   public Module resolve(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Module name was null");
      }
      return modules.get(name);
   }

   public Module create(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Module name was null");
      }
      Module current = modules.get(name);

      if (current == null) {
         Module module = new ContextModule(context, name);

         modules.put(name, module);
         exporter.export(module);
         
         return module;
      }
      return current;
   }
}
