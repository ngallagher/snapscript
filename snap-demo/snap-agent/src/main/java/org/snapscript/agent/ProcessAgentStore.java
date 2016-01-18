package org.snapscript.agent;

import java.io.InputStream;
import java.io.OutputStream;

import org.snapscript.core.resource.Store;

public class ProcessAgentStore implements Store {

   private String project;
   private Store store;
   
   public ProcessAgentStore(Store store) {
      this.store = store;
   }
   
   public void update(String project) {
      this.project = project;
   }

   @Override
   public InputStream getInputStream(String resource) {
      String path = getPath(resource);
      return store.getInputStream(path);
   }
   
   @Override
   public OutputStream getOutputStream(String resource) {
      String path = getPath(resource);
      return store.getOutputStream(path);
   }
   
   public String getPath(String resource) {
      if(project != null) {
         if(!project.startsWith("/")) {
            project = "/" + project;
         }
         if(!project.endsWith("/")) {
            project = project + "/";
         }
         return project + resource;
      }
      return resource;
   }
}
