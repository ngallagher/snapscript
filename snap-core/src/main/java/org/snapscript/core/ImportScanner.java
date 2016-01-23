package org.snapscript.core;

import static org.snapscript.core.Reserved.IMPORT_JAVA;
import static org.snapscript.core.Reserved.IMPORT_JAVAX;
import static org.snapscript.core.Reserved.IMPORT_JAVA_IO;
import static org.snapscript.core.Reserved.IMPORT_JAVA_LANG;
import static org.snapscript.core.Reserved.IMPORT_JAVA_MATH;
import static org.snapscript.core.Reserved.IMPORT_JAVA_NET;
import static org.snapscript.core.Reserved.IMPORT_JAVA_UTIL;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.lang.Package;

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
   private final Set<String> failures;
   private final String[] prefixes;
  
   public ImportScanner() {
      this(DEFAULTS);
   }
   
   public ImportScanner(String... prefixes) {
      this.packages = new ConcurrentHashMap<String, Package>();
      this.types = new ConcurrentHashMap<String, Class>();
      this.failures = new CopyOnWriteArraySet<String>();
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
                  return type;
               }
            }   
            failures.add(name); // not found!!
         }
         return type;
      }
      return null;
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
