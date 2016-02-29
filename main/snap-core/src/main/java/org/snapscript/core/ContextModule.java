package org.snapscript.core;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContextModule implements Module {

   private final Map<String, Type> imports;
   private final List<Function> functions; 
   private final List<Type> references;
   private final PathConverter converter;
   private final ImportManager manager;
   private final Context context;
   private final String prefix;
   private final String path;
   private final Scope scope;
   
   public ContextModule(Context context, String path, String prefix) {
      this.functions = new CopyOnWriteArrayList<Function>();
      this.imports = new ConcurrentHashMap<String, Type>();
      this.references = new CopyOnWriteArrayList<Type>();
      this.manager = new ImportManager(context, prefix);
      this.converter = new PathConverter();
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
         Type type = getType(name);
         
         if(type == null) {
            TypeLoader loader = context.getLoader();
            
            if(loader != null) {
               type = loader.defineType(prefix, name);
            }
            if(type != null) {
               imports.put(name, type);
               references.add(type);
            }
         }
         return type;
      } catch(Exception e){
         throw new ModuleException("Could not define '" + prefix + "." + name + "'", e);
      }
   }
   
   @Override
   public Module addImport(String name) {
      try {
         ModuleRegistry registry = context.getRegistry();
         Module module = registry.addModule(name);
         
         if(module != null) {
            manager.addImport(name); // add package "tetris.game."
         }
         return module;
      } catch(Exception e){
         throw new ModuleException("Could not import '" + name + "' in '" + prefix + "'", e);
      }
   }
   
   
   @Override
   public Type addImport(String module, String name) {
      try {
         TypeLoader loader = context.getLoader();
         Type type = loader.defineType(module, name);
         
         if(name != null) {
            imports.put(name, type);
            references.add(type);
         }
         return type;
      } catch(Exception e){
         throw new ModuleException("Could not import '" + module + "." + name + "' in '" + prefix + "'", e);
      }
   }

   @Override
   public Type addImport(String module, String name, String alias) {
      try {
         TypeLoader loader = context.getLoader();
         Type type = loader.defineType(module, name);
         
         if(name != null) {
            imports.put(alias, type);
            references.add(type);
         }
         return type;
      } catch(Exception e){
         throw new ModuleException("Could not import '" + module + "." + name + "' in '" + prefix + "'", e);
      }
   }
   
   @Override
   public Type getType(String name) {
      try {
         Type type = imports.get(name);
         
         if(type == null) {
            type = manager.getType(name);
            
            if(type != null) {
               imports.put(name, type);
               references.add(type);
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
   public String getPath() {
      try {
         return converter.createPath(prefix);
      } catch(Exception e){
         throw new ModuleException("Could not parse '" + prefix + "'", e);
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
   public String toString() {
      return prefix;
   }
}
