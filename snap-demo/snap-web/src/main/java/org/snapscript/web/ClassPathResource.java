package org.snapscript.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.web.resource.Resource;

public class ClassPathResource implements Resource {
   
   private final File tempPath;
   
   public ClassPathResource(File tempPath){
      this.tempPath = tempPath;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      String path = request.getPath().getPath();
      
      if(path.startsWith(".")) {
         path = path.substring(1);
      }
      File file = new File(tempPath, path);
      byte[] octets = loadFile(file, path);
      response.setContentType("text/plain");
      OutputStream out = response.getOutputStream();
      out.write(octets);
      out.close();
   }
   private byte[] loadFile(File file, String path) throws Exception {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      InputStream input = null;
      
      if(!file.exists()) {
         file = new File(tempPath, path);
      }
      if(file.exists()) {
         input = new FileInputStream(file);
      } else {
         input = ClassPathResource.class.getResourceAsStream(path);
      }
      byte[] chunk = new byte[1024];
      int count = 0;

      while((count = input.read(chunk)) != -1) {
         buffer.write(chunk, 0, count);
      }
      input.close();
      return buffer.toByteArray();
   }
}
