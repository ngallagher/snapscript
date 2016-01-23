package org.snapscript.core;

import static org.snapscript.core.Reserved.DEFAULT_PACKAGE;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class ContextModule implements Module {

   private final Map<String, Type> imports;
   private final List<Function> functions;   
   private final Set<String> modules;
   private final Context context;
   private final Scope scope;
   private final String name;
   
   public ContextModule(Context context, String name) {
      this.functions = new CopyOnWriteArrayList<Function>();
      this.imports = new ConcurrentHashMap<String, Type>();
      this.modules = new CopyOnWriteArraySet<String>();
      this.scope = new ModuleScope(this);
      this.context = context;
      this.name = name;
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
   public Type addType(String name) {
      try {
         Type t = getType(name);
         
         if(t == null) {
            TypeLoader loader = context.getLoader();
            t= loader.defineType(this.name, name);
            
            if(t!=null) {
               imports.put(name, t);
            }
         }
         return t;
      } catch(Exception e){
         throw new IllegalStateException(e);
      }
   }
   
   @Override
   public Module addImport(String module) {
      try {
         ModuleBuilder builder = context.getBuilder();
         Module result = builder.create(module);
         
         if(result != null) {
            modules.add(module); // add package "tetris.game."
         }
         return result;
      } catch(Exception e){
         throw new IllegalStateException(e);
      }
   }
   
   
   @Override
   public Type addImport(String module, String name) {
      try {
         TypeLoader loader = context.getLoader();
         Type type = loader.defineType(module, name);
         
         if(name != null && name.length() > 0) {
            imports.put(name, type);
         }
         return type;
      } catch(Exception e){
         throw new IllegalStateException(e);
      }
   }

   @Override
   public Type getType(String name) { // this needs to define the type also......
      try {
         Type type = imports.get(name);
         
         if(type == null) {
            TypeLoader loader = context.getLoader();
            Type result = loader.resolveType(this.name, name);
            
            if(result == null) {
               for(String prefix : modules) {
                  result = loader.resolveType(prefix, name); // this is "tetris.game.*"
               }
               if(result == null) {
                  result = loader.resolveType(null, name); // null is "java.*"
               }
               if(result == null) {
                  ModuleBuilder builder= context.getBuilder();
                  
                  for(String prefix : modules) {
                     Module module = builder.resolve(prefix);
                     
                     if(module != null) {
                        result = module.getType(name); // get imports from the outer module if it exists
                     }
                  }
               }
            }
            if(result != null) {
               imports.put(name, result);
            }
            return result;
         }
         return type;
      } catch(Exception e){
         throw new IllegalStateException(e);
      }
   }
   
   @Override
   public Type getType(Class type) {
      try {
         TypeLoader loader = context.getLoader();
         return loader.loadType(type);
      } catch(Exception e){
         throw new IllegalStateException(e);
      }
   }   
   
   @Override
   public InputStream getResource(String path) {
      try {
         ResourceManager manager = context.getManager();
         return manager.getInputStream(path);
      } catch(Exception e){
         throw new IllegalStateException(e);
      }
   }

   @Override
   public Scope getScope() {
      return scope;
   }
   
   @Override
   public String getName() {
      return name;
   }

   @Override
   public String toString() {
      return name;
   }
}
