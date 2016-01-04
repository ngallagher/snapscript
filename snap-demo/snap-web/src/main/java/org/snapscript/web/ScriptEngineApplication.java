package org.snapscript.web;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class ScriptEngineApplication {

   public static void main(String[] list) throws Exception {
      if (list.length < 1) {
         System.out.println("usage: " + ScriptEngineApplication.class.getName() + " [configFile] [propertyFile 1] .. [propertyFile N]");
         System.exit(-1);
      }
      Resource configFile = new FileSystemResource(list[0]);

      if (!configFile.exists()) {
         System.out.println("error: " + ScriptEngineApplication.class.getName() + " could not resolve config file " + list[0]);
         System.exit(-1);
      }
      Resource[] propertyFiles = new Resource[list.length - 1];

      for (int i = 0; i < propertyFiles.length; i++) {
         Resource resource = new FileSystemResource(list[i + 1]);

         if (!resource.exists()) {
            System.out.println("error: " + ScriptEngineApplication.class.getName() + " could not resolve property file " + list[i + 1]);
            System.exit(-1);
         }
         propertyFiles[i] = resource;
      }
      ApplicationContextLoader service = new ApplicationContextLoader(configFile, propertyFiles);
      service.start();
   }
}
