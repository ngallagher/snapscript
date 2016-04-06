package org.snapscript.develop.complete;

import java.io.PrintStream;
import java.util.Map;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.develop.http.project.Project;
import org.snapscript.develop.http.project.ProjectBuilder;
import org.snapscript.develop.http.resource.Resource;

import com.google.gson.Gson;

// /complete/<project>
public class AutoCompleteResource implements Resource {

   private final ProjectBuilder builder;
   private final AutoCompleter completer;
   private final Gson gson;
   
   public AutoCompleteResource(ProjectBuilder builder) {
      this.completer = new AutoCompleter();
      this.gson = new Gson();
      this.builder = builder;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      AutoCompleteResponse result = new AutoCompleteResponse();
      PrintStream out = response.getPrintStream();
      String content = request.getContent();
      Path path = request.getPath();
      Project project = builder.createProject(path);
      AutoCompleteRequest context = gson.fromJson(content, AutoCompleteRequest.class);
      String prefix = context.getPrefix();
      String source = context.getSource();
      String resource = context.getResource();
      Map<String, String> tokens = completer.complete(project, source, resource, prefix);
      result.setTokens(tokens);
      String text = gson.toJson(result);
      response.setContentType("application/json");
      out.println(text);
      out.close();
   }
}
