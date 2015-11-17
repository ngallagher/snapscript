package org.snapscript.core.resource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileReader implements ResourceReader {

   private final File file;
   
   public FileReader(File file) {
      this.file = file;
   }
   
   @Override
   public String read(String path) throws Exception {
      try {
         File resource = new File(file, path);
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
      } catch(Exception e) {
         throw new IOException("Could not load resource '" + path + "'", e);
      }
   }
}
