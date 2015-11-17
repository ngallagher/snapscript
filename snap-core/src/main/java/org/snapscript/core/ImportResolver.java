package org.snapscript.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.snapscript.core.resource.ResourceReader;

public class ImportResolver {
   
   private static final String[] DEFAULTS = {
      "",
      "java.", 
      "java.lang.", 
      "java.util.", 
      "java.util.concurrent.", 
      "java.util.concurrent.atomic.", 
      "java.util.concurrent.locks.",
      "java.io.",
      "java.net.",     
      "java.math."};
   
   private final Map<String, Class> types;
   private final List<String> imports;
   private final LibraryLoader loader;
   
   public ImportResolver(PackageLinker linker, ResourceReader reader) {
      this(linker, reader, Arrays.asList(DEFAULTS));
   }
   
   public ImportResolver(PackageLinker linker, ResourceReader reader, List<String> imports) {
      this.types = new LinkedHashMap<String, Class>();
      this.imports = new ArrayList<String>(imports);
      this.loader = new LibraryLoader(linker, reader);
   }
   
   public Package addImport(String name) {
      try {
         imports.add(name);
         return loader.load(name);
      } catch(Exception e){
         throw new IllegalStateException("Problem importing " + name);
      }
   }
   
   public Package addType(String name, String location) {
      for(String prefix : imports) {
         try {
            String title=prefix+location+"."+name;
            System.err.println("ImportResolver.addType() searching for class ["+title+"]");
            Class type = Class.forName(title);
            System.err.println("ImportResolver.addType() found class ["+type+"]");
            types.put(name, type); 
            types.put(location+"."+name, type);           
            return null;
         } catch(Exception e) {
            continue;
         }
      }
      try {
         return loader.load(location);
      } catch(Exception e){
         throw new IllegalStateException("Problem importing '" + name + "'",e);
      }
   }
   
   public Class getType(String name) {  
      Class type = types.get(name);
      
      if(type == null) {
         for(String prefix : imports) {
            try {
               return Class.forName(prefix + name);
            } catch(Exception e) {
               continue;
            }
         }         
      }
      return type;
   }
}
