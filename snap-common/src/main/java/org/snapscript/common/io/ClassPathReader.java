package org.snapscript.common.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathReader implements ResourceReader {
   
   private final Class type;
   
   public ClassPathReader(Class type) {
      this.type = type;
   }
   
   public String read(String path) throws Exception {
      try {
         ClassLoader loader = type.getClassLoader();
         InputStream source = loader.getResourceAsStream(path);
         
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
         throw new IOException("Could not load resource '" + path + "'", e);
      }
   }
}
