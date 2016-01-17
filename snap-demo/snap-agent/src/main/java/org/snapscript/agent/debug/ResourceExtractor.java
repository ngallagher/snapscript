package org.snapscript.agent.debug;

public class ResourceExtractor {
   
   private static final String SOURCE_PREFIX = "/src/";
   private static final String SOURCE_SUFFIX = ".snap";

   public static String extractModule(String path) {
      int length = path.length();
      
      if(path.endsWith(SOURCE_SUFFIX)) {
         path = path.substring(0, length - 5);
      }
      if(path.startsWith(SOURCE_PREFIX)) {
         path = path.substring(4 + 1);
      }
      return path.replace('/', '.');
   }
   
   public static String extractResource(String module) {
      String path = module;
      
      if(!path.startsWith(SOURCE_PREFIX)) {
         path = SOURCE_PREFIX + path;
      }
      if(!path.endsWith(SOURCE_SUFFIX)) {
         path = path.replace('.', '/');
         path = path + SOURCE_SUFFIX;
      }
      return path;
   }
}
