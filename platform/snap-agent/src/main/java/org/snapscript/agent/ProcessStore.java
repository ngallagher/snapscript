package org.snapscript.agent;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.snapscript.core.store.RemoteStore;
import org.snapscript.core.store.Store;

public class ProcessStore implements Store {

   private String project;
   private Store store;
   
   public ProcessStore(URI root) {
      this.store = new RemoteStore(root);
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
         if(resource.startsWith("/")) {
            resource = resource.substring(1);
         }
         return project + resource;
      }
      return resource;
   }
}
