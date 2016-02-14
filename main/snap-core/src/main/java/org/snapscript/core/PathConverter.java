package org.snapscript.core;

public class PathConverter {

   private final String suffix;
   
   public PathConverter(String suffix) {
      this.suffix = suffix;
   }
   
   public String convert(String path) {
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
