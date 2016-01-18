package org.snapscript.core.store;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.snapscript.core.Bug;

public class RemoteStore implements Store {

   private final LocationBuilder builder;
   private final URI root;
   
   public RemoteStore(URI root) {
      this.builder = new LocationBuilder(root);
      this.root = root;
   }
   
   @Override
   public InputStream getInputStream(String path) {  
      try {
         URL resource = builder.create(path);
         URLConnection connection = resource.openConnection();
         
         return connection.getInputStream();
      } catch(Exception e) {
         throw new StoreException("Could not load resource '" + path + "' from '" + root + "'", e);
      }
   }

   @Override
   public OutputStream getOutputStream(String path) {
      try {
         URL resource = builder.create(path);
         URLConnection connection = resource.openConnection();
         
         connection.setDoOutput(true);
         
         return connection.getOutputStream();
      } catch(Exception e) {
         throw new StoreException("Could not load resource '" + path + "' from '" + root + "'", e);
      }
   }
   
   @Bug("path is often http://host:port/x//y, i.e double slash")
   private static class LocationBuilder {
      
      private final URI root;
      
      public LocationBuilder(URI root) {
         this.root = root;
      }
      
      public URL create(String path) throws Exception {
         String original = root.getPath();
         String scheme = root.getScheme();
         String host = root.getHost();
         int port = root.getPort();
         
         if(!original.endsWith("/")) {
            original = original + "/";
         }
         if(path.startsWith("/")) {
            path = path.substring(1);
         }
         return new URL(scheme + "://" +  host + ":" + port + original + path);
      }
   }
}
