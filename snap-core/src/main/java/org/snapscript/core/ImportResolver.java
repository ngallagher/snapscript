package org.snapscript.core;
  
import static org.snapscript.core.Reserved.IMPORT_JAVA;
import static org.snapscript.core.Reserved.IMPORT_JAVAX;
import static org.snapscript.core.Reserved.IMPORT_JAVA_IO;
import static org.snapscript.core.Reserved.IMPORT_JAVA_LANG;
import static org.snapscript.core.Reserved.IMPORT_JAVA_MATH;
import static org.snapscript.core.Reserved.IMPORT_JAVA_NET;
import static org.snapscript.core.Reserved.IMPORT_JAVA_UTIL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.snapscript.core.resource.ResourceReader;

@Bug("This needs refactoring")
public class ImportResolver {
   
   private static final String[] DEFAULTS = {
      IMPORT_JAVA, 
      IMPORT_JAVAX,
      IMPORT_JAVA_LANG, 
      IMPORT_JAVA_UTIL, 
      IMPORT_JAVA_IO,
      IMPORT_JAVA_NET,     
      IMPORT_JAVA_MATH
   };
   
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
      Class type = getType(location+"."+name);
      
      if(type != null) {
         types.put(name, type); 
         types.put(location+"."+name, type);
         return null;
      }
//      for(String prefix : imports) {
//         try {
//            String title=prefix+location+"."+name;
//            Class type = Class.forName(title);
//            types.put(name, type); 
//            types.put(location+"."+name, type);           
//            return null;
//         } catch(Exception e) {
//            continue;
//         }
//      }
      try {
         return loader.load(location);
      } catch(Exception e){
         throw new IllegalStateException("Problem importing '" + name + "'",e);
      }
   }
   
   public Class getType(String name) {  
      Class type = types.get(name);
      
      if(type == null) {
         try {
            return Class.forName(name);
         }catch(Exception e){
            //e.printStackTrace();
         }
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
