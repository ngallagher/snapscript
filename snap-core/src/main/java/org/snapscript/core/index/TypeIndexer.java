package org.snapscript.core.index;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.snapscript.core.Bug;
import org.snapscript.core.ImportResolver;
import org.snapscript.core.NoStatement;
import org.snapscript.core.Package;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

@Bug("This is rubbish and needs to be cleaned up")
public class TypeIndexer {

   private final Map<Object, Type> types;
   private final ImportResolver resolver;
   private final ClassIndexer indexer;
   private final List<String> modules;
   
   public TypeIndexer(ImportResolver resolver){
      this.types = new LinkedHashMap<Object, Type>(); 
      this.modules = new ArrayList<String>();
      this.indexer = new ClassIndexer(this);
      this.resolver = resolver;
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
   protected void registerType(Object name, Type type) {
      types.put(name, type);
   }
   protected Type resolveType(Object name) {
      return types.get(name);
   }
   private Type define(String name,String moduleName) throws Exception {
      String full=createName(name, moduleName);
      Type t  =resolveType(full);
    
      
      if(t==null) {
         t=new Type(name, moduleName,null);
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
         registerType(name, t);
      }
      return t;
   }
   public Type load(final Class cls) throws Exception {
      return indexer.index(cls);
   }

}
