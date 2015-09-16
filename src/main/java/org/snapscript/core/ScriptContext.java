package org.snapscript.core;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.core.convert.FunctionBinder;
import org.snapscript.core.execute.LibraryLinker;
import org.snapscript.core.execute.ScriptLinker;

public class ScriptContext implements Context {
   
   private final Map<String, Module> modules;
   private final FunctionBinder binder;
   private final TypeResolver resolver;
   private final LibraryLinker linker;
   private final TypeLoader loader;   
   private final Module module;

   public ScriptContext(){
      this.modules = new HashMap<String, Module>();
      this.linker = new ScriptLinker(this);
      this.module = new ContextModule(this);
      this.resolver = new TypeResolver(linker);      
      this.loader = new TypeLoader(resolver);
      this.binder = new FunctionBinder(loader);
   }

   @Override
   public Module getModule() {
      return module;
   }
   
   @Override
   public Module getModule(String name) {
      return modules.get(name);
   }   

   @Override
   public Module addModule(String name) {
      Module current = modules.get(name);
      
      if(current == null) {
         Module module = new ContextModule(this, name);
         
         if(name != null) {           
            modules.put(name, module);
         }
         return module;
      }
      return current;
   }   


   @Override
   public FunctionBinder getBinder() {
      return binder;
   }

   @Override
   public TypeLoader getLoader() {
      return loader;
   }

   @Override
   public LibraryLinker getLinker() {
      return linker;
   }   
}
