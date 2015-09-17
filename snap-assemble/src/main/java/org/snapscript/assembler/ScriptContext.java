package org.snapscript.assembler;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.core.Context;
import org.snapscript.core.ContextModule;
import org.snapscript.core.FunctionBinder;
import org.snapscript.core.LibraryLinker;
import org.snapscript.core.Module;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.TypeResolver;

public class ScriptContext implements Context {
   
   private final Map<String, Module> modules;
   private final FunctionBinder binder;
   private final TypeResolver resolver;
   private final LibraryLinker linker;
   private final TypeLoader loader;   
   private final Module module;

   public ScriptContext(InstructionResolver res){
      this.modules = new HashMap<String, Module>();
      this.linker = new ScriptLinker(res, this);
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
