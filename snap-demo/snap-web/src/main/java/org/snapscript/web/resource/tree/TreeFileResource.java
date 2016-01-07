package org.snapscript.web.resource.tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.web.resource.Resource;

public class TreeFileResource implements Resource {

   private final File resourcePath;
   
   public TreeFileResource(File resourcePath) {
      this.resourcePath = resourcePath;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      Path path = request.getPath();
      String normal = path.getPath();
      String file = normal.replace('/', File.separatorChar);
      File resource = new File(resourcePath, file);
      response.setContentType("text/plain");
      OutputStream out = response.getOutputStream();
      FileInputStream stream = new FileInputStream(resource);
      byte[]chunk = new byte[1024];
      int count = 0;
      try {
         while((count = stream.read(chunk))!=-1){
            out.write(chunk,0,count);
         }
      }finally {
         stream.close();
         out.close();
      }
   }
}
