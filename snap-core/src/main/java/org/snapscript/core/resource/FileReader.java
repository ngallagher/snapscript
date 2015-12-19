package org.snapscript.core.resource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileReader implements ResourceReader {

   private final ClassPathReader reader;
   private final File file;
   
   public FileReader(File file) {
      this.reader = new ClassPathReader();
      this.file = file;
   }
   
   @Override
   public String read(String path) {
      try {
         File resource = new File(file, path);
         
         if(resource.exists()) {
            InputStream source = new FileInputStream(resource);
            
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
         }
      } catch(Exception e) {
         throw new ResourceException("Could not load resource '" + path + "'", e);
      }
      return reader.read(path);
   }
}
