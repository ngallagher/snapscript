package org.snapscript.develop.complete;

import java.io.PrintStream;
import java.util.Set;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.develop.http.project.Project;
import org.snapscript.develop.http.project.ProjectBuilder;
import org.snapscript.develop.http.resource.Resource;

// /format/<project>
public class AutoFormatResource implements Resource {
   
   private static final int DEFAULT_INDENT = 3;

   private final AutoFormatter formatter;
   private final ProjectBuilder builder;
   
   public AutoFormatResource(ProjectBuilder builder) {
      this.formatter = new AutoFormatter();
      this.builder = builder;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      PrintStream out = response.getPrintStream();
      String content = request.getContent();
      Path path = request.getPath();
      String token = request.getParameter("indent");
      Integer indent = token == null ? DEFAULT_INDENT : Integer.parseInt(token);
      Project project = builder.createProject(path);
      String result = formatter.format(project, content, indent);
      response.setContentType("text/plain");
      out.println(result);
      out.close();
   }
}
