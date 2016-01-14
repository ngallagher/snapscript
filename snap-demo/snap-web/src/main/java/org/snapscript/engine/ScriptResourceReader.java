package org.snapscript.engine;

import org.snapscript.core.resource.ResourceReader;

public class ScriptResourceReader implements ResourceReader {

   private ResourceReader reader;
   private String project;
   
   public ScriptResourceReader(ResourceReader reader) {
      this.reader = reader;
   }
   
   public void update(String project) {
      this.project = project;
   }

   @Override
   public String read(String resource) {
      if(project != null) {
         if(!project.startsWith("/")) {
            project = "/" + project;
         }
         if(!project.endsWith("/")) {
            project = project + "/";
         }
         return reader.read(project + resource);
      }
      return reader.read(resource);
   }
}
