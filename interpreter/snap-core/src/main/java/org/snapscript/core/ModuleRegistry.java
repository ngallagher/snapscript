package org.snapscript.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.snapscript.core.export.SystemExporter;

public class ModuleRegistry {

   private final Map<String, Module> modules;
   private final List<Module> references;
   private final SystemExporter exporter;
   private final Context context;

   public ModuleRegistry(Context context) {
      this.modules = new ConcurrentHashMap<String, Module>();
      this.references = new CopyOnWriteArrayList<Module>();
      this.exporter = new SystemExporter(context);
      this.context = context;
   }
   
   public List<Module> getModules() {
      return references;
   }

   public Module getModule(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Module name was null");
      }
      return modules.get(name);
   }

   public Module addModule(String name) {
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
