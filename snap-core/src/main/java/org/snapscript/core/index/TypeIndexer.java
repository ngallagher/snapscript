package org.snapscript.core.index;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.snapscript.core.Bug;
import org.snapscript.core.ImportResolver;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.NoStatement;
import org.snapscript.core.Package;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

@Bug("This is rubbish and needs to be cleaned up")
public class TypeIndexer {

   private final Map<Object, Type> types;
   private final ImportResolver resolver;
   private final ModuleBuilder builder;
   private final ClassIndexer indexer;
   private final List<String> modules;
   private final TypeCache cache;
   
   public TypeIndexer(ImportResolver resolver, ModuleBuilder builder){
      this.types = new LinkedHashMap<Object, Type>(); 
      this.modules = new ArrayList<String>();
      this.cache = new TypeCache();
      this.indexer = new ClassIndexer(builder, this, cache);
      this.resolver = resolver;
      this.builder = builder;
   }
   public Package addImport(String name) {
      modules.add(name);///XXX????
      return resolver.addImport(name);
   }   
   public Package addType(final String name, final String module) {
      
      //modules.add(module);
      //resolver.addImport(location);// helps with java types
      
      Package library = resolver.addType(name, module);
      if(library == null) {
         return new Package() {

            @Override
            public Statement compile(Scope scope) throws Exception {
               String full = createName(name, module);
               load(name, module);
               return new NoStatement();
            }
            
         };
      }
      return library;
   }
   private void registerType(Object name, Type type) {
      if(types.containsKey(name)) {
         throw new IllegalStateException("Key " + name + " already registered");
      }
      types.put(name, type);
   }
   private Type resolveType(Object name) {
      return types.get(name);
   }
   private Type define(String name,String moduleName) throws Exception {
      String full=createName(name, moduleName);
      Type t  =resolveType(full);
    
      
      if(t==null) {
         Module module = builder.resolve(moduleName);
         
         if(module == null) {
            throw new IllegalArgumentException("Module '"+moduleName+"' does not exist");
         }
         t=new ScopeType(module, name);
         registerType(full,t);
      }
      return t;
   }
   private String createName(String name, String module){
      if(module != null && module.length() >0) {
         return module + "." + name;
      }
      return name;
   }
   
   public Type load(String name,String moduleName) throws Exception {
      return load(name,moduleName,true); // XXX moduleName was null here?????
   }
   public Type load(String nameX, String moduleName, boolean create) throws Exception {
      String name = createName(nameX, moduleName);
      Type t = resolveType(name);
      
      if(t==null) { 
         Class cls=resolver.getType(name);
         if(cls==null){
            for(String n:modules){// was the type named in a module???
               t=resolveType(n+"."+name);
               if(t!=null){
                  return t;
               }
            }
            if(create){
               return define(nameX,moduleName);
            }
            return null;
         }
         t=load(cls);
      }
      return t;
   }
   @Bug("Add to module")
   public Type load(final Class cls) throws Exception {
      Type done=resolveType(cls);
      if(done == null) {
         String name = cls.getName();
         Type type = new ClassReference(indexer, cls, cls.getSimpleName());
         registerType(cls, type);
         registerType(name, type);
        
         return type;
      }
      return done;
   }

}
