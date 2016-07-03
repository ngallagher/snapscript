package org.snapscript.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.snapscript.core.extend.ModuleExtender;

public class ModuleRegistry {

   private final Map<String, Module> modules;
   private final List<Module> references;
   private final PathConverter converter;
   private final ModuleExtender extender;
   private final Context context;

   public ModuleRegistry(Context context) {
      this.modules = new ConcurrentHashMap<String, Module>();
      this.references = new CopyOnWriteArrayList<Module>();
      this.extender = new ModuleExtender(context);
      this.converter = new PathConverter();
      this.context = context;
   }
   
   public synchronized List<Module> getModules() {
      return references;
   }

   public synchronized Module getModule(String name) {
      if (name == null) {
         throw new InternalArgumentException("Module name was null");
      }
      return modules.get(name);
   }

   public synchronized Module addModule(String name) {
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
   
   public synchronized Module addModule(String name, String path) {
      if (name == null) {
         throw new InternalArgumentException("Module name was null");
      }
      Module current = modules.get(name);

      if (current == null) {
         Module module = new ContextModule(context, path, name);

         modules.put(name, module);
         extender.extend(module);
         references.add(module);
         
         return module;
      }
      return current;
   }
}
