package org.snapscript.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.snapscript.core.export.SystemExporter;

public class ModuleRegistry {

   private final Map<String, Module> modules;
   private final List<Module> references;
   private final PathConverter converter;
   private final SystemExporter exporter;
   private final Context context;

   public ModuleRegistry(Context context) {
      this.modules = new ConcurrentHashMap<String, Module>();
      this.references = new CopyOnWriteArrayList<Module>();
      this.exporter = new SystemExporter(context);
      this.converter = new PathConverter();
      this.context = context;
   }
   
   public List<Module> getModules() {
      return references;
   }

   public Module getModule(String name) {
      if (name == null) {
         throw new InternalArgumentException("Module name was null");
      }
      return modules.get(name);
   }

   public Module addModule(String name) {
      if (name == null) {
         throw new InternalArgumentException("Module name was null");
      }
      String path = converter.createPath(name);
      Module current = modules.get(name);
      
      if(current == null) {
         return addModule(name, path);
      }
      return current;
   }
   
   public Module addModule(String name, String path) {
      if (name == null) {
         throw new InternalArgumentException("Module name was null");
      }
      Module current = modules.get(name);

      if (current == null) {
         Module module = new ContextModule(context, path, name);

         modules.put(name, module);
         exporter.export(module);
         references.add(module);
         
         return module;
      }
      return current;
   }
}
