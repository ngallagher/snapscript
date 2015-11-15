package org.snapscript.run;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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
      Class type = getClass();
      ClassLoader loader = type.getClassLoader();
      InputStream stream = loader.getResourceAsStream(library);
      
      if(stream == null) {
         throw new IllegalStateException("Could not find library '" + library + "'");
      }
      return stream;
   }
}
