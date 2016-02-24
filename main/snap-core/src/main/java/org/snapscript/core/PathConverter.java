package org.snapscript.core;

import static org.snapscript.core.Reserved.SCRIPT_EXTENSION;

public class PathConverter {

   private final String suffix;
   
   public PathConverter() {
      this(SCRIPT_EXTENSION);
   }
   
   public PathConverter(String suffix) {
      this.suffix = suffix;
   }
   
   public String createPath(String resource) {
      int index = resource.indexOf(suffix);
      
      if(index == -1) {
         int slash = resource.indexOf('.');
      
         if(slash != -1) {
            resource = resource.replace('.', '/');
         }
         return "/" + resource + suffix;
      }
      return resource;
   }
   
   public String createModule(String path) {
      int index = path.indexOf(suffix);

      if(index != -1) {
         path = path.substring(0, index);
      }
      if(path.startsWith("/")) {
         path = path.substring(1);
      }
      if(path.startsWith("\\")) {
         path = path.substring(1);
      }
      if(path.contains("\\")) {
         path = path.replace("\\", ".");
      }
      return path.replace('/',  '.');
   }
}
