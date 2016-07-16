package org.snapscript.core;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContextModule implements Module {
   
   private final Map<String, Module> modules;
   private final List<Annotation> annotations;   
   private final Map<String, Type> types;
   private final List<Function> functions; 
   private final List<Type> references;
   private final ImportManager manager;
   private final Context context;
   private final String prefix;
   private final String path;
   private final Scope scope;
   
   public ContextModule(Context context, String path, String prefix) {
      this.annotations = new CopyOnWriteArrayList<Annotation>();
      this.functions = new CopyOnWriteArrayList<Function>();
      this.modules = new ConcurrentHashMap<String, Module>();
      this.types = new ConcurrentHashMap<String, Type>();
      this.references = new CopyOnWriteArrayList<Type>();
      this.manager = new ImportManager(context, prefix);
      this.scope = new ModuleScope(this);
      this.context = context;
      this.prefix = prefix;
      this.path = path;
   }

   @Override
   public Context getContext() {
      return context;
   }
   
   @Override
   public ImportManager getManager() {
      return manager;
   }
   
   @Override
   public List<Annotation> getAnnotations() {
      return annotations;
   }

   @Override
   public List<Function> getFunctions() {
      return functions;
   }
   
   @Override
   public List<Type> getTypes() {
      return references;
   }
   
   @Override
   public Type addType(String name) {
      try {
         Type type = types.get(name); 
         
         if(type == null) {
            TypeLoader loader = context.getLoader();
            
            if(loader != null) {
               type = loader.defineType(prefix, name);
            }
            if(type != null) {
               types.put(name, type);
               references.add(type);
            }
         }
         return type;
      } catch(Exception e){
         throw new ModuleException("Could not define '" + prefix + "." + name + "'", e);
      }
   }
   
   @Override
   public Module getModule(String name) {
      try {
         Module module = modules.get(name);
         
         if(module == null) {
            if(!types.containsKey(name)) { // don't resolve if its a type
               module = manager.getModule(name); // import tetris.game.*
               
               if(module != null) {
                  modules.put(name, module);
               }
            }
         }
         return module;
      } catch(Exception e){
         throw new ModuleException("Could not find '" + name + "' in '" + prefix + "'", e);
      }
   }
   
   @Override
   public Type getType(String name) {
      try {
         Type type = types.get(name);
         
         if(type == null) {
            if(!modules.containsKey(name)) {// don't resolve if its a module
               type = manager.getType(name);
            
               if(type != null) {
                  types.put(name, type);
                  references.add(type);
               }
            }
         }
         return type;
      } catch(Exception e){
         throw new ModuleException("Could not find '" + name + "' in '" + prefix + "'", e);
      }
   }
   
   @Override
   public Type getType(Class type) {
      try {
         TypeLoader loader = context.getLoader();
         
         if(loader != null) {
            return loader.loadType(type);
         }
         return null;
      } catch(Exception e){
         throw new ModuleException("Could not load " + type, e);
      }
   }   
   
   @Override
   public InputStream getResource(String path) {
      try {
         ResourceManager manager = context.getManager();

         if(manager != null) {
            return manager.getInputStream(path);
         }
         return null;
      } catch(Exception e){
         throw new ModuleException("Could not load file '" + path + "'", e);
      }
   }

   @Override
   public Scope getScope() {
      return scope;
   }
   
   @Override
   public String getName() {
      return prefix;
   }
   
   @Override
   public String getPath() {
      return path;
   }

   @Override
   public String toString() {
      return prefix;
   }
}
