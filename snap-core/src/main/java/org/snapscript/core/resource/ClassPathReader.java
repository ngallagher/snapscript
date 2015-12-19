package org.snapscript.core.resource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ClassPathReader implements ResourceReader {
   
   public ClassPathReader() {
      super();
   }
   
   public String read(String path) {
      try {
         Thread thread = Thread.currentThread();
         ClassLoader loader = thread.getContextClassLoader();
         InputStream source = loader.getResourceAsStream(path);
         
         if(source == null) {
            throw new IllegalArgumentException("Could not find '" + path + "'");
         }
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
         throw new ResourceException("Could not load resource '" + path + "'", e);
      }
   }
}
