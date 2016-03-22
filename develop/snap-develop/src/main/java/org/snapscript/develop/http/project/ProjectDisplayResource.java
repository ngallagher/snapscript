package org.snapscript.develop.http.project;

import java.io.File;
import java.io.PrintStream;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.xml.core.Persister;
import org.snapscript.develop.http.resource.Resource;

import com.google.gson.Gson;

// /theme/<project>
public class ProjectDisplayResource implements Resource {
   
   private final ProjectBuilder builder;
   private final Persister persister;
   private final String theme;
   private final Gson gson;
   
   public ProjectDisplayResource(ProjectBuilder builder, String theme) {
      this.persister = new Persister();
      this.gson = new Gson();
      this.builder = builder;
      this.theme = theme;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      Path path = request.getPath(); 
      Project project = builder.createProject(path);
      PrintStream out = response.getPrintStream();
      File root = project.getProjectPath();
      File file = new File(root, theme);
      
      if(!file.exists()) {
         root = root.getParentFile();
         file = new File(root, theme);
      }
      if(file.exists()) {
         ProjectDisplay theme = persister.read(ProjectDisplay.class, file);
         String text = gson.toJson(theme);
         response.setStatus(Status.OK);
         response.setContentType("application/json");
         out.println(text);
         out.close();
      } else {
         response.setStatus(Status.NOT_FOUND);
         response.setContentType("text/html");
         out.println("Not found!");
         out.close();
      }
   }

}
