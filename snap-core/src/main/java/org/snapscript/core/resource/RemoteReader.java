package org.snapscript.core.resource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class RemoteReader implements ResourceReader {

   private final LocationBuilder builder;
   private final URI root;
   
   public RemoteReader(URI root) {
      this.builder = new LocationBuilder(root);
      this.root = root;
   }
   
   @Override
   public String read(String path) {  
      try {
         URL resource = builder.create(path);
         InputStream source = resource.openStream();

         try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[1024];
            int count = 0;
            
            while((count = source.read(chunk)) != -1) {
               buffer.write(chunk, 0, count);
            }
            return buffer.toString("UTF-8");
         } finally {
            source.close();
         }
      } catch(Exception e) {
         throw new ResourceException("Could not load resource '" + path + "' from '" + root + "'", e);
      }
   }
   
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
         if(path.endsWith("/")) {
            path = path.substring(1);
         }
         return new URL(scheme + "://" +  host + ":" + port + original + path);
      }
   }
}
