package org.snapscript.core;

import static org.snapscript.core.Reserved.IMPORT_JAVA;
import static org.snapscript.core.Reserved.IMPORT_JAVAX;
import static org.snapscript.core.Reserved.IMPORT_JAVA_IO;
import static org.snapscript.core.Reserved.IMPORT_JAVA_LANG;
import static org.snapscript.core.Reserved.IMPORT_JAVA_MATH;
import static org.snapscript.core.Reserved.IMPORT_JAVA_NET;
import static org.snapscript.core.Reserved.IMPORT_JAVA_UTIL;

import java.lang.Package;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImportScanner {
   
   private static final String[] DEFAULTS = {
      IMPORT_JAVA, 
      IMPORT_JAVAX,
      IMPORT_JAVA_LANG, 
      IMPORT_JAVA_UTIL, 
      IMPORT_JAVA_IO,
      IMPORT_JAVA_NET,     
      IMPORT_JAVA_MATH
   };
   
   private final Map<String, Package> packages;
   private final Map<String, Class> types;
   private final Map<Object, String> names;
   private final Set<String> failures;
   private final String[] prefixes;
  
   public ImportScanner() {
      this(DEFAULTS);
   }
   
   public ImportScanner(String... prefixes) {
      this.packages = new HashMap<String, Package>();
      this.names = new HashMap<Object, String>();
      this.types = new HashMap<String, Class>();
      this.failures = new HashSet<String>();
      this.prefixes = prefixes;
   }
   
   public Package importPackage(String name) {
      if(!failures.contains(name)) {
         Package result = packages.get(name);
         
         if(result == null) {
            result = loadPackage(name);
         }
         if(result == null) {
            for(String prefix : prefixes) {
               result = loadPackage(prefix + name);
               
               if(result != null) {
                  packages.put(name, result);
                  names.put(result, name);
                  return result;
               }
            }   
            failures.add(name); // not found!!
         }
         return result;
      }
      return null;
   }

   public Class importType(String name) {
      if(!failures.contains(name)) {
         Class type = types.get(name);
         
         if(type == null) {
            type = loadType(name);
         }
         if(type == null) {
            for(String prefix : prefixes) {
               type = loadType(prefix + name);
               
               if(type != null) {
                  types.put(name, type);
                  names.put(type, name);
                  return type;
               }
            }   
            failures.add(name); // not found!!
         }
         return type;
      }
      return null;
   }
   
   public String importName(Class type) {
      String result = names.get(type);
      
      if(result == null) {
         String absolute = type.getName();
         
         for(String prefix : prefixes) {
            if(absolute.startsWith(prefix)) {
               int length = prefix.length();
               String name = absolute.substring(length);
               
               types.put(absolute, type);
               types.put(name, type);
               names.put(type, name);
               
               return name;
            }
            types.put(absolute, type);
            names.put(type, absolute);
         }   
         return absolute;
      }
      return result;
   }
   
   private Class loadType(String name) {
      try {
         Class result = Class.forName(name);

         if(result != null) {
            types.put(name, result);
         }
         return result;
      }catch(Exception e){
         return null;
      }
   }
   
   private Package loadPackage(String name) {
      try {
         Package result = Package.getPackage(name);
         
         if(result != null) {
            packages.put(name, result);
         }
         return result;
      }catch(Exception e){
         return null;
      }
   }
}
