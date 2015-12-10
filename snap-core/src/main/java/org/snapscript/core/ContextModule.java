package org.snapscript.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContextModule implements Module {

   private final Map<String, Type> imports;
   private final List<Function> functions;    
   private final Context context;
   private final Scope scope;
   private final String name;

   public ContextModule(Context context) {
      this(context, null);
   }
   
   public ContextModule(Context context, String name) {
      this.functions = new CopyOnWriteArrayList<Function>();
      this.imports = new ConcurrentHashMap<String, Type>();
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
            t= loader.defineType(name, this.name);
            
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
   public Type addImport(String name, String module) {
      try {
         TypeLoader loader = context.getLoader();
         Type type = loader.defineType(name, module);
         
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
            Type result = loader.resolveType(name, this.name);
            
            if(result == null) {
               result = loader.resolveType(name, null);
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
