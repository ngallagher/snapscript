package org.snapscript.run;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.snapscript.core.InternalStateException;

public class SourceLoader {

   public String load(String file) throws Exception {
      InputStream source = open(file);
      
      try {
         ByteArrayOutputStream buffer = new ByteArrayOutputStream();
         byte[] chunk = new byte[1024];
         int count = 0;
    
         while((count = source.read(chunk)) !=- 1){
            buffer.write(chunk, 0, count);
         }
         return buffer.toString("UTF-8");
      } finally {
         source.close();         
      }  
   }
   
   private InputStream open(String library) throws Exception {
      File file = new File(library);
      
      if(!file.exists()) {
         Class type = getClass();
         ClassLoader loader = type.getClassLoader();
         InputStream stream = loader.getResourceAsStream(library);
         String location = file.getCanonicalPath();
         
         if(stream == null) {
            throw new InternalStateException("Could not find library '" + location + "'");
         }
         return stream;
      }
      return new FileInputStream(file);
   }
}
